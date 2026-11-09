package com.pwyl.modules.pay.apple.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 苹果内购时间工具：项目统一使用 GMT+8 的 yyyy-MM-dd HH:mm:ss 字符串。
 */
final class IapTimeUtils {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId ZONE_GMT8 = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter GMT8_FORMATTER = DateTimeFormatter.ofPattern(PATTERN).withZone(ZONE_GMT8);

    private IapTimeUtils() {
    }

    static String now() {
        return new SimpleDateFormat(PATTERN).format(new Date());
    }

    /**
     * 将苹果返回的 ISO-8601 Instant(如 2026-09-30T03:00:00Z) 转为 GMT+8 字符串。
     */
    static String formatInstant(String isoInstant) {
        if (isoInstant == null || isoInstant.trim().isEmpty()) {
            return null;
        }
        try {
            return GMT8_FORMATTER.format(Instant.parse(isoInstant.trim()));
        } catch (Exception e) {
            try {
                // 兼容毫秒时间戳字符串
                long epochMilli = Long.parseLong(isoInstant.trim());
                return GMT8_FORMATTER.format(Instant.ofEpochMilli(epochMilli));
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    /**
     * 解析 GMT+8 字符串时间，异常时返回当前时间，保证支付成功后能正常开通。
     */
    static Date parseLenient(String text) {
        if (text != null && !text.trim().isEmpty()) {
            try {
                return new SimpleDateFormat(PATTERN).parse(text.trim());
            } catch (Exception ignored) {
            }
        }
        return new Date();
    }
}
