package com.teleo.manager.notification.services;

import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.notification.dto.reponse.NotificationResponse;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import com.teleo.manager.notification.enums.TypeNotification;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface NotificationService extends ServiceGeneric<NotificationRequest, NotificationResponse, Notification> {

    // Génération des notifications
    void generateNotification(Account emetteur, Account destinataire, String titre, String message, TypeNotification type);

    // Récupération des notifications d'un utilisateur
    List<NotificationResponse> findAllNotificationsByUserId(Long userId);
    List<NotificationResponse> findUnreadNotificationsByUserId(@NotNull Long userId);
    List<NotificationResponse> markAllNotificationsAsReadByUserId(@NotNull Long userId);
}
