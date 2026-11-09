package com.kongling.iap.gateway;

public interface AppleGateway {
    AppleVerifiedTransaction verifyTransaction(AppleVerificationInput input);
    AppleNotificationData verifyNotification(String signedPayload);
}
