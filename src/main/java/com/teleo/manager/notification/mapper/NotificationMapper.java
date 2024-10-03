package com.teleo.manager.notification.mapper;

import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.notification.dto.reponse.NotificationResponse;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AccountService.class})
public interface NotificationMapper extends GenericMapper<NotificationRequest, NotificationResponse, Notification> {
}
