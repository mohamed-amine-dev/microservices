package com.realestate.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {
    private Long recipientId;
    private String recipientEmail;
    private String subject;
    private String message;
    private String type; // e.g., RENTAL_CREATED, PAYMENT_RECEIVED
}
