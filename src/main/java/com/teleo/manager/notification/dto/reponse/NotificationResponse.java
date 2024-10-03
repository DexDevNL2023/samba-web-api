package com.teleo.manager.notification.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.notification.enums.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResponse extends BaseResponse {
    private Boolean lu;
    private String titre;
    private String message;
    private LocalDateTime dateEnvoi;
    private TypeNotification type;
    private Long destinataire;
    private Long emetteur;
}
