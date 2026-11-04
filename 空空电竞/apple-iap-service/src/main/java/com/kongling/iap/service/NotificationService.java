package com.kongling.iap.service;

import com.kongling.iap.gateway.AppleGateway;
import com.kongling.iap.gateway.AppleNotificationData;
import com.kongling.iap.persistence.AppleNotificationRepository;
import com.kongling.iap.persistence.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    private final AppleGateway appleGateway;
    private final AppleNotificationRepository notificationRepository;
    private final OutboxRepository outboxRepository;

    public NotificationService(AppleGateway appleGateway,
                               AppleNotificationRepository notificationRepository,
                               OutboxRepository outboxRepository) {
        this.appleGateway = appleGateway;
        this.notificationRepository = notificationRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public boolean process(String signedPayload) {
        AppleNotificationData notification = appleGateway.verifyNotification(signedPayload);
        boolean inserted = notificationRepository.insert(notification, signedPayload);
        if (inserted) outboxRepository.insertNotificationEvent(notification);
        return inserted;
    }
}
