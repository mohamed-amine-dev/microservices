package com.realestate.notification.service;

import com.realestate.notification.dto.NotificationRequest;
import com.realestate.notification.entity.Notification;
import com.realestate.notification.entity.NotificationStatus;
import com.realestate.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification sendNotification(NotificationRequest request) {
        log.info("Sending notification to: {} | Subject: {}", request.getRecipientEmail(), request.getSubject());
        log.info("Message body: {}", request.getMessage());

        // Simulating the actual email sending process
        try {
            log.debug("Connecting to SMTP server...");
            Thread.sleep(100); // Simulate network latency
            log.info("Email successfully sent to {}", request.getRecipientEmail());
        } catch (InterruptedException e) {
            log.error("Notification interrupted", e);
        }

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(NotificationStatus.SENT)
                .build();

        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
