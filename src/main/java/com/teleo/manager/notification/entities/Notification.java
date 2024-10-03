package com.teleo.manager.notification.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.enums.TypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity<Notification, NotificationRequest> {

    private static final String ENTITY_NAME = "NOTIFICATION";
    private static final String MODULE_NAME = "NOTIFICATION_MODULE";

    @Column(nullable = false)
    private Boolean lu = false;

    @Column(nullable = false)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;

    @Column(length = 5000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinataire_id")
    private Account destinataire;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emetteur_id")
    private Account emetteur;

    // Méthodes pour savoir si le système est l'émetteur ou le destinataire
    public boolean isSystemEmetteur() {
        return this.emetteur != null && this.emetteur.getId().equals(AppConstants.SYSTEM_ACCOUNT_ID);
    }

    public boolean isSystemDestinataire() {
        return this.destinataire != null && this.destinataire.getId().equals(AppConstants.SYSTEM_ACCOUNT_ID);
    }

    @Override
    public void update(Notification source) {
        this.lu = source.getLu();
        this.titre = source.getTitre();
        this.type = source.getType();
        this.message = source.getMessage();
        this.dateEnvoi = source.getDateEnvoi();

        // Mise à jour des entités associées
        this.destinataire = source.getDestinataire();
        this.emetteur = source.getEmetteur();
    }

    @Override
    public boolean equalsToDto(NotificationRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = (lu == null ? source.getLu() == null : lu.equals(source.getLu())) &&
                titre.equals(source.getTitre()) &&
                type.equals(source.getType()) &&
                ((message == null && source.getMessage() == null) ||
                        (message != null && message.equals(source.getMessage()))) &&
                dateEnvoi.equals(source.getDateEnvoi());

        // Comparaison des entités associées
        boolean isDestinataireEqual = (destinataire == null && source.getDestinataire() == null) ||
                (destinataire != null && destinataire.getId().equals(source.getDestinataire()));

        boolean isEmetteurEqual = (emetteur == null && source.getEmetteur() == null) ||
                (emetteur != null && emetteur.getId().equals(source.getEmetteur()));

        return areFieldsEqual && isDestinataireEqual && isEmetteurEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + getId() +
                ", titre='" + titre + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", destinataire=" + (isSystemDestinataire() ? "SYSTEM" : destinataire) +
                ", emetteur=" + (isSystemEmetteur() ? "SYSTEM" : emetteur) +
                ", lu=" + lu +
        '}';
    }

}
