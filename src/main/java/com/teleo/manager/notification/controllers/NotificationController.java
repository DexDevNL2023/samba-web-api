package com.teleo.manager.notification.controllers;

import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.notification.dto.reponse.NotificationResponse;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface NotificationController extends ControllerGeneric<NotificationRequest, NotificationResponse, Notification> {
    ResponseEntity<RessourceResponse<List<NotificationResponse>>> getNotificationsByUserId(@NotNull @PathVariable("userId") Long userId);
    ResponseEntity<RessourceResponse<List<NotificationResponse>>> getUnreadNotificationsByUserId(@NotNull @PathVariable("userId") Long userId);
    ResponseEntity<RessourceResponse<List<NotificationResponse>>> markAsReadNotificationsByUserId(@NotNull @PathVariable("userId") Long userId);
}
