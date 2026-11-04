package com.pwyl.modules.pay.apple.dto;

import lombok.Data;

/**
 * 调用 Java11 验证服务 /ack 接口的请求体。
 */
@Data
public class Java11AckRequest {
    private Long userId;
    private String businessOrderNo;
}
