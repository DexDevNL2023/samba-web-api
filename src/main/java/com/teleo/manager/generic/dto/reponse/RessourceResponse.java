package com.teleo.manager.generic.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class RessourceResponse<T> {

    private boolean success;
    private String message;
    private T content;

    public RessourceResponse(String message) {
        this.success = true;
        this.message = message;
        this.content = null;
    }

    public RessourceResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.content = null;
    }

    public RessourceResponse(String message, T content) {
        this.success = true;
        this.message = message;
        this.content = content;
    }

    public RessourceResponse(Boolean success, T content) {
        this.success = success;
        this.message = "Une erreur est survenue pendant le traitement de votre requête.";
        this.content = content;
    }
}
