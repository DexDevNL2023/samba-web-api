package com.teleo.manager.authentification.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.authentification.security.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utilisateurs")
public class Account extends BaseEntity<Account, AccountRequest> implements UserDetails {

    private static final String ENTITY_NAME = "UTILISATEUR";

    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String langKey;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private Boolean usingQr;

    private String loginUrl;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private Boolean actived;

    @Column(nullable = false)
    private Boolean connected;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String resetPasswordToken;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String accesToken;

    public void addRoles(Role newRole) {
        if (!this.roles.contains(newRole)) {
            // Associer le rôle à ce compte
            newRole.setAccount(this);
            this.roles.add(newRole);
        }
    }

    // Nouvelle méthode pour ajouter des rôles à un compte
    public void addRoles(List<Role> newRoles) {
        for (Role newRole : newRoles) {
            // Vérifier si le rôle est déjà présent
            if (!this.roles.contains(newRole)) {
                // Associer le rôle à ce compte
                newRole.setAccount(this);

                this.roles.add(newRole);
            }
        }
    }

    //userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityUtils.buildUserAuthorities(this.authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.actived;
    }

    @Override
    public void update(Account source) {
        this.fullName = source.getFullName();
        this.email = source.getEmail();
        this.langKey = source.getLangKey();
        this.login = source.getLogin();
        this.usingQr = source.getUsingQr();
        this.loginUrl = source.getLoginUrl();
        this.imageUrl = source.getImageUrl();
        this.authority = source.getAuthority();
        this.actived = source.getActived();
        this.connected = source.getConnected();
        this.password = source.getPassword(); // Note: Si la gestion des mots de passe nécessite une logique supplémentaire, elle doit être ajoutée ici.
        this.resetPasswordToken = source.getResetPasswordToken();
        this.accesToken = source.getAccesToken();

        // Mise à jour des rôles
        this.roles.clear();
        if (source.getRoles() != null) {
            this.roles.addAll(source.getRoles());
        }
    }

    @Override
    public boolean equalsToDto(AccountRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = fullName.equals(source.getFullName()) &&
                email.equals(source.getEmail()) &&
                langKey.equals(source.getLangKey()) &&
                login.equals(source.getLogin()) &&
                loginUrl.equals(source.getLoginUrl()) &&
                imageUrl.equals(source.getImageUrl()) &&
                authority == source.getAuthority();

        // Comparaison des listes de rôles (basée sur la taille)
        boolean areRolesEqual = (source.getRoles() == null && roles.isEmpty()) ||
                (source.getRoles() != null && source.getRoles().size() == roles.size());

        return areFieldsEqual && areRolesEqual;
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
        return "Compte{" +
                "id=" + getId() +
                ", nomComplet='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", activé=" + actived +
                ", autorité=" + authority +
                ", langue='" + langKey + '\'' +
                ", rôles=" + roles.stream()
                .map(role -> "Rôle{id=" + role.getId() + ", clé='" + role.getRoleKey() + "', libellé='" + role.getLibelle() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
