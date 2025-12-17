package com.realestate.notification.controller;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.notification.dto.NotificationRequest;
import com.realestate.notification.entity.Notification;
import com.realestate.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Service")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Send a notification (email mock)")
    public ResponseEntity<ResponseWrapper<Notification>> sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(ResponseWrapper.success("Notification sent successfully",
                notificationService.sendNotification(request)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notification history for user")
    public ResponseEntity<ResponseWrapper<List<Notification>>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(ResponseWrapper.success(notificationService.getNotificationsByUser(userId)));
    }
}
