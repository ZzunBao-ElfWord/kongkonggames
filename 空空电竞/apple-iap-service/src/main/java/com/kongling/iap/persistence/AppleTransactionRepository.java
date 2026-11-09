package com.kongling.iap.persistence;

import com.kongling.iap.domain.ProductDefinition;
import com.kongling.iap.gateway.AppleVerifiedTransaction;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class AppleTransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    public AppleTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insertVerified(long userId, ProductDefinition product, AppleVerifiedTransaction transaction) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO apple_transaction " +
                            "(transaction_id, original_transaction_id, user_id, product_id, product_type, environment, " +
                            "purchase_date, expires_date, revocation_date, quantity, grant_value, status, raw_signed_transaction) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'VERIFIED_PENDING_GRANT', ?)",
                    transaction.getTransactionId(),
                    transaction.getOriginalTransactionId(),
                    userId,
                    product.getProductId(),
                    product.getType().name(),
                    transaction.getEnvironment(),
                    timestamp(transaction.getPurchaseDate()),
                    timestamp(transaction.getExpiresDate()),
                    timestamp(transaction.getRevocationDate()),
                    Math.max(1, transaction.getQuantity()),
                    product.getGrantValue(),
                    transaction.getSignedTransaction()
            );
            return true;
        } catch (DuplicateKeyException ignored) {
            return false;
        }
    }

    public Optional<StoredTransaction> find(String transactionId) {
        List<StoredTransaction> rows = jdbcTemplate.query(
                "SELECT transaction_id, user_id, product_id, status FROM apple_transaction WHERE transaction_id = ?",
                (rs, rowNum) -> new StoredTransaction(
                        rs.getString("transaction_id"),
                        rs.getLong("user_id"),
                        rs.getString("product_id"),
                        rs.getString("status")
                ),
                transactionId
        );
        return rows.stream().findFirst();
    }

    public boolean markGranted(String transactionId, long userId, String businessOrderNo) {
        return jdbcTemplate.update(
                "UPDATE apple_transaction SET status = 'GRANTED', business_order_no = COALESCE(?, business_order_no), " +
                        "updated_at = CURRENT_TIMESTAMP " +
                        "WHERE transaction_id = ? AND user_id = ? AND status IN ('VERIFIED_PENDING_GRANT', 'GRANTED')",
                businessOrderNo,
                transactionId,
                userId
        ) == 1;
    }

    private Timestamp timestamp(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
