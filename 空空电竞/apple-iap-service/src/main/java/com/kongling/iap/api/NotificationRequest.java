package com.kongling.iap.api;

import javax.validation.constraints.NotBlank;

public class NotificationRequest {
    @NotBlank
    private String signedPayload;

    public String getSignedPayload() { return signedPayload; }
    public void setSignedPayload(String signedPayload) { this.signedPayload = signedPayload; }
}
