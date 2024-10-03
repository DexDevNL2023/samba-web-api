package com.teleo.manager.notification.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.notification.controllers.NotificationController;
import com.teleo.manager.notification.dto.reponse.NotificationResponse;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import com.teleo.manager.notification.services.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API de gestion des notifications")
public class NotificationControllerImpl extends ControllerGenericImpl<NotificationRequest, NotificationResponse, Notification> implements NotificationController {

    private static final String MODULE_NAME = "NOTIFICATION_MODULE";

    private final NotificationService service;
    private final AuthorizationService authorizationService;

    public NotificationControllerImpl(NotificationService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Notification newInstance() {
        return new Notification();
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<NotificationResponse>>> getNotificationsByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Les notifications ont été récupérées avec succès", service.findAllNotificationsByUserId(userId)), HttpStatus.OK);
    }

    // Récupérer les notifications non lues d'un utilisateur
    @GetMapping(value = "/unread/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<NotificationResponse>>> getUnreadNotificationsByUserId(@NotNull @PathVariable("userId") Long userId) {
        //authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        List<NotificationResponse> unreadNotifications = service.findUnreadNotificationsByUserId(userId);
        return new ResponseEntity<>(new RessourceResponse<>("Les notifications non lues ont été récupérées avec succès", unreadNotifications), HttpStatus.OK);
    }

    // Marquer toutes les notifications d'un utilisateur comme lues
    @PostMapping(value = "/read/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<NotificationResponse>>> markAsReadNotificationsByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.WRITE_PERMISSION));
        List<NotificationResponse> updatedNotifications = service.markAllNotificationsAsReadByUserId(userId);
        return new ResponseEntity<>(new RessourceResponse<>("Toutes les notifications ont été marquées comme lues", updatedNotifications), HttpStatus.OK);
    }
}
