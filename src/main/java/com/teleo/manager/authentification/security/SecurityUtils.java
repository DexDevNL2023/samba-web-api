package com.teleo.manager.authentification.security;

import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.utils.AppConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Classe utilitaire pour Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Obtenez le login de l'utilisateur actuel.
     *
     * @renvoie le login de l'utilisateur actuel.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Obtenez le JWT de l'utilisateur actuel.
     *
     * @renvoie le JWT de l'utilisateur actuel.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object credentials = authentication.getCredentials();
        if (credentials instanceof String) {
            return Optional.of((String) credentials);
        }
        return Optional.empty();
    }

    /**
     * Vérifiez si un utilisateur est authentifié.
     *
     * @return true si l'utilisateur est authentifié, false sinon.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AppConstants.ANONYMOUS::equals);
    }

    /**
     * Vérifie si l'utilisateur actuel possède l'une des autorités.
     *
     * @param authorities les autorisations à vérifier.
     * @return true si l'utilisateur actuel possède l'une des autorités, false sinon.
     */
    public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (
                authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
        );
    }

    /**
     * Vérifie si l'utilisateur actuel n'a aucune des autorités.
     *
     * @param authorities les autorisations à vérifier.
     * @return true si l'utilisateur actuel ne dispose d'aucune autorité, false sinon.
     */
    public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
        return !hasCurrentUserAnyOfAuthorities(authorities);
    }

    /**
     * Vérifie si l'utilisateur actuel dispose d'une autorité spécifique.
     *
     * @param authority l'autorisation à vérifier.
     * @return true si l'utilisateur actuel a l'autorité, false sinon.
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        return hasCurrentUserAnyOfAuthorities(authority);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

    public static List<SimpleGrantedAuthority> buildUserAuthorities(Authority authority) {
        if (authority == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(authority.name()));
    }
}
