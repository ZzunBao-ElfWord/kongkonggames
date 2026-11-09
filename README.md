# kongkonggames · 空空电竞

本仓库保存空空电竞项目的脱敏源码快照，包含本地尚未提交的开发修改。源码统一位于 `空空电竞/`。

| 目录 | 内容 |
| --- | --- |
| `空空电竞/peiwan-uniapp/` | Android / uni-app 客户端 |
| `空空电竞/peiwan-uniapp-ios/` | iOS / uni-app 客户端 |
| `空空电竞/peiwan-vue/` | 管理后台 |
| `空空电竞/src/` | Java 业务后端 |
| `空空电竞/website/kongling/` | 官网、隐私政策与用户协议等网页 |
| `空空电竞/apple-iap-service/` | 独立 Java 11 苹果内购验单服务 |
| `空空电竞/deployment/scripts/` | 部署启动脚本，配套运行配置须自行提供 |

## 使用前必读

- 这是源码快照，不是可直接覆盖生产服务器的部署包。
- 密钥、口令等敏感值已从公开副本中删除或替换。`REDACTED_CONFIGURE_LOCALLY` 表示需要自行提供的本地配置，不是可用凭据。
- 本地运行时请通过不提交的配置文件或环境变量提供数据库账号、云服务及支付凭据、SDK 配置与内部服务鉴权令牌。Java 8 和 Java 11 内购服务须配置相同的内部令牌。
- 不包含生产数据库数据、原 Git 历史、日志、证书、私钥、备份、依赖缓存或生成的安装包。
- 本次整理和发布不修改原项目，不重启生产服务，不改变线上支付环境，也不代表 App Store 审核或正式支付已通过验证。
- 代码保留已有开发状态；敏感信息扫描通过不等同于完整的构建、功能或安全审计。

更多说明见 [公开源码配置说明](空空电竞/PUBLIC_SOURCE_NOTES.md)。
## 原仓库产品规划（保留）

> **ZzunBao-ElfWord** 产品矩阵 · AI 电竞直播子项目

## 产品矩阵

| 仓库 | 中文名 | 定位 | 状态 |
|------|--------|------|------|
| [openelf](https://github.com/ZzunBao-ElfWord/openelf) | 开放精灵 | AI 网红创作平台主 App | ✅ 主开发 |
| [hualing](https://github.com/ZzunBao-ElfWord/hualing) | 画灵儿 | Pure-Dart 开源绘图引擎 | ✅ 开源已发布 |
| [koko](https://github.com/ZzunBao-ElfWord/koko) | 空空 | 空空主 App | 🚧 规划中 |
| [kongkongmusic](https://github.com/ZzunBao-ElfWord/kongkongmusic) | 空空音乐 | AI 音乐生成子项目 | 🚧 规划中 |
| [**kongkonggames**](https://github.com/ZzunBao-ElfWord/kongkonggames) | 空空电竞 | AI 电竞直播与互动子项目 | 已导入现有空空电竞脱敏源码；AI 方向为原规划 |
| [linkelf](https://github.com/ZzunBao-ElfWord/linkelf) | 链接精灵 | FISCO BCOS 联盟链能力层 | 🚧 规划中 |

### 原规划技术栈（非本次导入源码）

- **语言**: Dart 3.x · Rust
- **框架**: Flutter
- **直播**: WebSocket · Edge-TTS · 画面合成

## 仓库状态

已导入 `空空电竞/` 源码。当前实现使用 uni-app / Vue 和 Java / Spring Boot，与上面的 Flutter / Rust 规划区分维护。

## License

© 2026 ZzunBao-ElfWord · All Rights Reserved
