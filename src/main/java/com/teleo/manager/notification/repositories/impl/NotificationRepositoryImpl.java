package com.teleo.manager.notification.repositories.impl;

import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepositoryImpl extends GenericRepositoryImpl<NotificationRequest, Notification> {
    public NotificationRepositoryImpl(EntityManager entityManager) {
        super(Notification.class, entityManager);
    }
}
