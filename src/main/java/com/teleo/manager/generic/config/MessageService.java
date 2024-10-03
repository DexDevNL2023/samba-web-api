package com.teleo.manager.generic.config;

import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Resource
    private MessageSource messageSource;

    /**
     * Retourne un message avec le code spécifié. Si le message n'est pas trouvé,
     * renvoie le message par défaut fourni.
     *
     * @param code Le code du message.
     * @param defaultMessage Le message par défaut si aucun message n'est trouvé.
     * @return Le message localisé ou le message par défaut.
     */
    public String getMessage(String code, String defaultMessage) {
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }

    /**
     * Retourne un message avec des paramètres et un code spécifié. Si le message n'est pas trouvé,
     * renvoie le message par défaut fourni.
     *
     * @param code Le code du message.
     * @param defaultMessage Le message par défaut si aucun message n'est trouvé.
     * @param params Les paramètres pour le message.
     * @return Le message localisé avec les paramètres ou le message par défaut.
     */
    public String getMessage(String code, String defaultMessage, Object... params) {
        return messageSource.getMessage(code, params, defaultMessage, locale);
    }
}
