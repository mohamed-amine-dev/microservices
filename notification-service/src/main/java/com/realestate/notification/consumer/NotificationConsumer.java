package com.realestate.notification.consumer;

import com.realestate.common.dto.NotificationEvent;
import com.realestate.notification.dto.NotificationRequest;
import com.realestate.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.notification.name:notification-queue}")
    public void consume(NotificationEvent event) {
        log.info(String.format("Received notification event -> %s", event.toString()));

        // Process the event
        try {
            NotificationRequest request = new NotificationRequest();
            request.setUserId(event.getRecipientId());
            request.setRecipientEmail(event.getRecipientEmail());
            request.setSubject(event.getSubject());
            request.setMessage(event.getMessage());

            notificationService.sendNotification(request);
        } catch (Exception e) {
            log.error("Failed to process notification event", e);
        }
    }
}
