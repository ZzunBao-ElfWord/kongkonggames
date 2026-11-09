package com.pwyl.modules.pay.apple.service;

/**
 * 苹果内购业务异常。
 * code 用于告知 App 该错误是终态还是可重试：
 * 202 处理中(可重试，禁止 finish)
 * 4002 永久失败(交易无效/伪造/退款/绑定他人，App 可 finish)
 * 5001 临时失败(验证服务/发货异常，可重试，禁止 finish)
 */
public class AppleIapException extends RuntimeException {
    public static final int CODE_PROCESSING = 202;
    public static final int CODE_PERMANENT = 4002;
    public static final int CODE_RETRY = 5001;

    private final int code;

    public AppleIapException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AppleIapException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public static AppleIapException processing(String message) {
        return new AppleIapException(CODE_PROCESSING, message);
    }

    public static AppleIapException permanent(String message) {
        return new AppleIapException(CODE_PERMANENT, message);
    }

    public static AppleIapException retry(String message) {
        return new AppleIapException(CODE_RETRY, message);
    }

    public static AppleIapException retry(String message, Throwable cause) {
        return new AppleIapException(CODE_RETRY, message, cause);
    }

    public int getCode() {
        return code;
    }
}
