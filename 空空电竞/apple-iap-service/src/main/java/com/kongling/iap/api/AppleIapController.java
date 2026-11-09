package com.kongling.iap.api;

import com.kongling.iap.service.NotificationService;
import com.kongling.iap.service.ProductCatalog;
import com.kongling.iap.service.TransactionVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
public class AppleIapController {
    private final TransactionVerificationService transactionService;
    private final NotificationService notificationService;
    private final ProductCatalog productCatalog;

    public AppleIapController(TransactionVerificationService transactionService,
                              NotificationService notificationService,
                              ProductCatalog productCatalog) {
        this.transactionService = transactionService;
        this.notificationService = notificationService;
        this.productCatalog = productCatalog;
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("service", "apple-iap-service");
        result.put("status", "UP");
        result.put("message", "Apple IAP verification service is running");
        result.put("health", "/actuator/health");
        return result;
    }

    @PostMapping("/internal/v1/apple/transactions/verify")
    public VerifyTransactionResponse verify(@Valid @RequestBody VerifyTransactionRequest request) {
        return transactionService.verify(request);
    }

    @PostMapping("/internal/v1/apple/transactions/{transactionId}/ack")
    public Map<String, Object> acknowledge(@PathVariable String transactionId,
                                           @Valid @RequestBody AckGrantRequest request) {
        transactionService.acknowledgeGrant(transactionId, request.getUserId(), request.getBusinessOrderNo());
        return Collections.singletonMap("success", true);
    }

    @GetMapping("/internal/v1/apple/products")
    public Object products() {
        return productCatalog.all().values();
    }

    @PostMapping("/public/v1/apple/notifications/v2")
    public ResponseEntity<Void> notification(@Valid @RequestBody NotificationRequest request) {
        notificationService.process(request.getSignedPayload());
        return ResponseEntity.ok().build();
    }
}
