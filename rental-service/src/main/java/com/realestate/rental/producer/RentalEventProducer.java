package com.realestate.rental.producer;

import com.realestate.common.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalEventProducer {

    @Value("${rabbitmq.exchange.name:realestate-exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key.notification:notification.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendNotificationEvent(NotificationEvent event) {
        log.info(String.format("Sending notification event -> %s", event.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
