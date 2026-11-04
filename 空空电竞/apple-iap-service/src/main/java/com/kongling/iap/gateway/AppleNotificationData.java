package com.kongling.iap.gateway;

public class AppleNotificationData {
    private String notificationUuid;
    private String notificationType;
    private String subtype;
    private String environment;
    private AppleVerifiedTransaction transaction;

    public String getNotificationUuid() { return notificationUuid; }
    public void setNotificationUuid(String notificationUuid) { this.notificationUuid = notificationUuid; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public String getSubtype() { return subtype; }
    public void setSubtype(String subtype) { this.subtype = subtype; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public AppleVerifiedTransaction getTransaction() { return transaction; }
    public void setTransaction(AppleVerifiedTransaction transaction) { this.transaction = transaction; }
}
