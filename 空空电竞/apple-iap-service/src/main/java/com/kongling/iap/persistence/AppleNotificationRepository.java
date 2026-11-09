package com.kongling.iap.persistence;

import com.kongling.iap.gateway.AppleNotificationData;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppleNotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public AppleNotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insert(AppleNotificationData notification, String signedPayload) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO apple_notification " +
                            "(notification_uuid, notification_type, subtype, environment, transaction_id, signed_payload, process_status, processed_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, 'VERIFIED', CURRENT_TIMESTAMP)",
                    notification.getNotificationUuid(),
                    notification.getNotificationType(),
                    notification.getSubtype(),
                    notification.getEnvironment(),
                    notification.getTransaction() == null ? null : notification.getTransaction().getTransactionId(),
                    signedPayload
            );
            return true;
        } catch (DuplicateKeyException ignored) {
            return false;
        }
    }
}
