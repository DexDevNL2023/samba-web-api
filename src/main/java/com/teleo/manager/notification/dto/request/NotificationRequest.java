package com.teleo.manager.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.notification.enums.TypeNotification;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequest extends BaseRequest {
    private Boolean lu = false;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @EnumValidator(enumClass = TypeNotification.class, message = "Le type de notification est invalide")
    private TypeNotification type;

    private String message;
    private LocalDateTime dateEnvoi;

    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataire;

    @NotNull(message = "L'émetteur est obligatoire")
    private Long emetteur;
}
