package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRequest extends BaseRequest {
    private String fullName;
	@NotBlank(message = "L'email est obligatoire")
    private String email;
    private String langKey;
    private String login;
    private Boolean usingQr=false;
    private String loginUrl;
    @Pattern(regexp = "^data:image/(png|jpg|jpeg);base64,[A-Za-z0-9+/=]*$", message = "Format d'image non valide")
    private String imageUrl;
    private Boolean actived;
    @EnumValidator(enumClass = Authority.class, message = "L'authorisation est invalide")
    private Authority authority;
    private List<Long> roles = new ArrayList<>();
}
