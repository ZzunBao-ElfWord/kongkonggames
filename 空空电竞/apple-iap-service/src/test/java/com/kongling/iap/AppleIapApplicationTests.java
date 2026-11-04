package com.kongling.iap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local-mock")
class AppleIapApplicationTests {
    private static final String TOKEN = "REDACTED_CONFIGURE_LOCALLY";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Test
    void internalEndpointsRequireToken() throws Exception {
        mockMvc.perform(get("/internal/v1/apple/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void exposesTheNineConfiguredProducts() throws Exception {
        mockMvc.perform(get("/internal/v1/apple/products").header("X-Internal-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)));
    }

    @Test
    void verifiesCoinPurchaseOnlyOnceAndAcknowledgesGrant() throws Exception {
        Map<String, Object> request = verification(1001L, "com.konglingapp.coin06", "test-coin-06");

        mockMvc.perform(post("/internal/v1/apple/transactions/verify")
                        .header("X-Internal-Token", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.firstProcessed").value(true))
                .andExpect(jsonPath("$.grantRequired").value(true))
                .andExpect(jsonPath("$.grantValue").value(42))
                .andExpect(jsonPath("$.environment").value("Sandbox"));

        mockMvc.perform(post("/internal/v1/apple/transactions/verify")
                        .header("X-Internal-Token", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstProcessed").value(false))
                .andExpect(jsonPath("$.grantRequired").value(true));

        Map<String, Object> ack = new LinkedHashMap<>();
        ack.put("userId", 1001L);
        ack.put("businessOrderNo", "ORDER-1001");
        mockMvc.perform(post("/internal/v1/apple/transactions/test-coin-06/ack")
                        .header("X-Internal-Token", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ack)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(post("/internal/v1/apple/transactions/verify")
                        .header("X-Internal-Token", TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstProcessed").value(false))
                .andExpect(jsonPath("$.grantRequired").value(false))
                .andExpect(jsonPath("$.status").value("GRANTED"));

        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT status, business_order_no FROM apple_transaction WHERE transaction_id = ?",
                "test-coin-06");
        org.junit.jupiter.api.Assertions.assertEquals("GRANTED", row.get("status"));
        org.junit.jupiter.api.Assertions.assertEquals("ORDER-1001", row.get("business_order_no"));
    }

    @Test
    void blocksReplayByAnotherUser() throws Exception {
        Map<String, Object> first = verification(2001L, "com.konglingapp.coin30", "replay-test");
        Map<String, Object> replay = verification(2002L, "com.konglingapp.coin30", "replay-test");

        verify(first).andExpect(status().isOk());
        verify(replay)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("TRANSACTION_OWNED_BY_ANOTHER_USER"));
    }

    @Test
    void rejectsUnknownProductBeforeGranting() throws Exception {
        verify(verification(3001L, "unknown.product", "unknown-test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("UNKNOWN_PRODUCT"));
    }

    @Test
    void verifiesMembershipProduct() throws Exception {
        verify(verification(4001L, "0002", "membership-quarter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productType").value("MEMBERSHIP"))
                .andExpect(jsonPath("$.grantValue").value(3))
                .andExpect(jsonPath("$.expiresDate").exists());
    }

    @Test
    void notificationRetriesAreIdempotent() throws Exception {
        String payload = "LOCAL_MOCK_NOTIFICATION:notification-1:DID_RENEW:0001:renewal-1";
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("signedPayload", payload);

        for (int i = 0; i < 2; i++) {
            mockMvc.perform(post("/public/v1/apple/notifications/v2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(request)))
                    .andExpect(status().isOk());
        }

        Integer notifications = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM apple_notification WHERE notification_uuid = ?", Integer.class, "notification-1");
        Integer outbox = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM apple_entitlement_outbox WHERE event_key = ?", Integer.class,
                "APPLE_NOTIFICATION:notification-1");
        org.junit.jupiter.api.Assertions.assertEquals(1, notifications);
        org.junit.jupiter.api.Assertions.assertEquals(1, outbox);
    }

    private org.springframework.test.web.servlet.ResultActions verify(Map<String, Object> request) throws Exception {
        return mockMvc.perform(post("/internal/v1/apple/transactions/verify")
                .header("X-Internal-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));
    }

    private Map<String, Object> verification(long userId, String productId, String transactionId) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("userId", userId);
        request.put("productId", productId);
        request.put("transactionId", transactionId);
        return request;
    }
}
