package com.kongling.iap.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongling.iap.gateway.AppleNotificationData;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OutboxRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public OutboxRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void insertNotificationEvent(AppleNotificationData notification) {
        String eventKey = "APPLE_NOTIFICATION:" + notification.getNotificationUuid();
        try {
            String payload = objectMapper.writeValueAsString(notification);
            jdbcTemplate.update(
                    "INSERT INTO apple_entitlement_outbox " +
                            "(event_key, event_type, transaction_id, product_id, payload, status) VALUES (?, ?, ?, ?, ?, 'PENDING')",
                    eventKey,
                    notification.getNotificationType(),
                    notification.getTransaction() == null ? null : notification.getTransaction().getTransactionId(),
                    notification.getTransaction() == null ? null : notification.getTransaction().getProductId(),
                    payload
            );
        } catch (DuplicateKeyException ignored) {
            // Apple retries notifications. The event key makes these retries harmless.
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize Apple notification", exception);
        }
    }
}
