package com.teleo.manager.notification.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.notification.dto.request.NotificationRequest;
import com.teleo.manager.notification.entities.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends GenericRepository<NotificationRequest, Notification> {

    // Méthode optimisée pour récupérer les notifications d'un utilisateur/assuré selon les trois critères
    @Query("SELECT DISTINCT n FROM Notification n " +
            "WHERE (n.emetteur.id = :userId AND n.destinataire.id = :systemAccountId) " + // Critère 3: Assuré -> Système
            "   OR (n.emetteur.id = :systemAccountId AND n.destinataire.id = :userId) " + // Critère 1: Système -> Assuré
            "   OR (n.emetteur.id = :systemAccountId AND n.destinataire.id = :systemAccountId) " + // Critère 2: Système -> Système
            "   OR (n.emetteur.id != :userId AND n.destinataire.id = :userId)") // Critère 1: Autre utilisateur -> Assuré
    List<Notification> findAllByUserId(@Param("userId") Long userId, @Param("systemAccountId") Long systemAccountId);

    // Méthode pour récupérer les notifications non lues pour un utilisateur
    @Query("SELECT n FROM Notification n " +
            "WHERE (n.emetteur.id = :userId AND n.destinataire.id = :systemAccountId) " +
            "   OR (n.emetteur.id = :systemAccountId AND n.destinataire.id = :userId) " +
            "   OR (n.emetteur.id = :systemAccountId AND n.destinataire.id = :systemAccountId) " +
            "   OR (n.emetteur.id != :userId AND n.destinataire.id = :userId)" +
            "AND n.lu = false")
    List<Notification> findUnreadNotificationsByUserId(@Param("userId") Long userId, @Param("systemAccountId") Long systemAccountId);
}
