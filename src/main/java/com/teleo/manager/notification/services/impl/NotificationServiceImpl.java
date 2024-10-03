package com.teleo.manager.notification.services.impl;

import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.repositories.AccountRepository;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.notification.dto.reponse.NotificationResponse;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.mapper.NotificationMapper;
import com.teleo.manager.notification.repositories.NotificationRepository;
import com.teleo.manager.notification.services.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl extends ServiceGenericImpl<NotificationRequest, NotificationResponse, Notification> implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final AccountRepository accountRepository;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper, AccountRepository accountRepository) {
        super(Notification.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @LogExecution
    @Override
    public void generateNotification(Account emetteur, Account destinataire, String titre, String message, TypeNotification type) {
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setDateEnvoi(LocalDateTime.now());

        // Vérification si l'émetteur ou le destinataire est le système
        Account systemAccount = accountRepository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));

        notification.setEmetteur(emetteur != null ? emetteur : systemAccount); // Système comme émetteur par défaut
        notification.setDestinataire(destinataire != null ? destinataire : systemAccount); // Système comme destinataire par défaut

        repository.save(notification);
    }

    @Transactional
    @LogExecution
    @Override
    public List<NotificationResponse> findAllNotificationsByUserId(Long userId) {
        List<Notification> notifications = repository.findAllByUserId(userId, AppConstants.SYSTEM_ACCOUNT_ID);
        return mapper.toDto(notifications);
    }

    @Transactional
    @LogExecution
    @Override
    public List<NotificationResponse> findUnreadNotificationsByUserId(Long userId) {
        List<Notification> unreadNotifications = repository.findUnreadNotificationsByUserId(userId, AppConstants.SYSTEM_ACCOUNT_ID);
        return mapper.toDto(unreadNotifications);
    }

    @Transactional
    @LogExecution
    @Override
    public List<NotificationResponse> markAllNotificationsAsReadByUserId(Long userId) {
        List<Notification> notifications = repository.findAllByUserId(userId, AppConstants.SYSTEM_ACCOUNT_ID);

        for (Notification notification : notifications) {
            notification.setLu(true); // Assuming 'lu' is the field to mark as read
        }

        repository.saveAll(notifications);
        return mapper.toDto(notifications);
    }
}
