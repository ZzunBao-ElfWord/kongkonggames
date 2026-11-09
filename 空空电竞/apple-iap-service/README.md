# 空灵 Apple 订单验证服务

这是一个独立的 Java 11 服务，不修改现有 Java 8 后端。它使用 Apple 官方 `app-store-server-library-java` 完成：

- Apple 交易 JWS 签名验证
- 根据 transactionId 调用 App Store Server API
- 生产环境查不到交易时，自动查询沙盒环境
- Bundle ID、产品 ID、交易 ID、退款状态和会员有效期校验
- transactionId 全局唯一，防止重复发币或给另一用户发放
- App Store Server Notifications V2 签名验证和重复通知去重
- 交易与通知持久化，并用 outbox 表衔接原有业务后端

## 产品对照

| Apple 产品 ID | 类型 | 发放内容 |
|---|---|---:|
| `com.konglingapp.coin01` | 柚子 | 7 |
| `com.konglingapp.coin06` | 柚子 | 42 |
| `com.konglingapp.coin30` | 柚子 | 210 |
| `com.konglingapp.coin100` | 柚子 | 700 |
| `com.konglingapp.coin500` | 柚子 | 3500 |
| `com.konglingapp.coin1000` | 柚子 | 7000 |
| `0001` | 会员 | 1 个月 |
| `0002` | 会员 | 3 个月 |
| `0003` | 会员 | 12 个月 |

## 本地模拟测试

双击 `start-local-mock.cmd` 可以启动本地模拟服务，默认地址为 `http://127.0.0.1:8091`。模拟模式不连 Apple，用于先验证接口、数据库、重复订单防护和旧后端的接入流程。

查看健康状态：

```powershell
Invoke-RestMethod http://127.0.0.1:8091/actuator/health
```

模拟验证一笔购买：

```powershell
$headers = @{ 'X-Internal-Token' = 'REDACTED_CONFIGURE_LOCALLY' }
$body = @{
  userId = 10001
  productId = 'com.konglingapp.coin06'
  transactionId = 'local-transaction-001'
} | ConvertTo-Json
Invoke-RestMethod -Method Post `
  -Uri http://127.0.0.1:8091/internal/v1/apple/transactions/verify `
  -Headers $headers -ContentType 'application/json' -Body $body
```

业务后端发完柚子或开通会员后，调用确认接口：

```powershell
$ack = @{ userId = 10001; businessOrderNo = 'OLD-SERVER-ORDER-001' } | ConvertTo-Json
Invoke-RestMethod -Method Post `
  -Uri http://127.0.0.1:8091/internal/v1/apple/transactions/local-transaction-001/ack `
  -Headers $headers -ContentType 'application/json' -Body $ack
```

## 真实 Apple 沙盒测试

从 App Store Connect / Users and Access / Integrations / In-App Purchase 创建密钥，准备：

- Issuer ID
- Key ID
- 下载的 `.p8` 私钥（Apple 只提供一次下载）
- App Store Connect 中的数字 App Apple ID
- Apple Root CA 证书文件，建议从 Apple PKI 页面下载并在服务器本地保存

设置环境变量后直接启动 jar，不要启用 `local-mock` profile：

```powershell
$env:APPLE_BUNDLE_ID='com.kongling.app'
$env:APPLE_APP_ID='App Store Connect 中的数字 ID'
$env:APPLE_ISSUER_ID='Issuer ID'
$env:APPLE_KEY_ID='Key ID'
$env:APPLE_PRIVATE_KEY_PATH='REDACTED_CONFIGURE_LOCALLY'
$env:APPLE_ROOT_CERT_PATHS='D:\apple-secrets\AppleRootCA-G3.cer'
$env:IAP_INTERNAL_TOKEN='替换为高强度随机字符串'
java -jar target\apple-iap-service-0.1.0-SNAPSHOT.jar
```

真机购买后，iOS 或旧后端将 `userId`、`productId`、`transactionId`传给验证接口。服务会先查生产，只有 Apple 明确返回“交易不存在”时才查沙盒。

## 与旧 Java 8 后端的接入顺序

1. iOS 购买成功，将 Apple `transactionId` 发给旧后端。
2. 旧后端用内网调用本服务 `/internal/v1/apple/transactions/verify`。
3. 返回 `valid=true` 且 `grantRequired=true` 时，旧后端在自己的数据库中发币或开通会员。旧后端也必须将 Apple `transactionId` 设为唯一键，保证重试时不会重复发放。
4. 旧后端提交业务事务后，调用 `/ack` 记录其业务订单号。
5. `firstProcessed` 只表示是否首次写入验证库。真正的发放判断使用 `grantRequired`；调用 `/ack` 后它会变为 `false`。
6. Apple V2 通知写入 `apple_entitlement_outbox`，后续由同步任务把续费、退款、过期等状态同步到旧后端。

## 数据库

本地默认是 H2，上服务器可通过下列环境变量切换 MySQL：

```text
DB_URL=jdbc:mysql://127.0.0.1:3306/apple_iap?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
DB_USERNAME=...
DB_PASSWORD=...
DB_DRIVER=com.mysql.cj.jdbc.Driver
```

`schema.sql` 会创建独立的三张表，不要与旧后端共用交易表。

## 编译和自动测试

```powershell
mvn test
mvn package
```

自动测试包括：内部接口鉴权、9 个产品映射、柚子订单、会员订单、重复交易、跨用户重放、未知产品和重复 Apple 通知。
