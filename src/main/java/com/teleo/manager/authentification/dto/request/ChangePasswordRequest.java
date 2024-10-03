package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.teleo.manager.generic.validators.password.PasswordMatches;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordRequest {
    @NotBlank(message = "Veillez renseigner votre mot de passe actuel svp!")
    private String currentPassword;
    @NotBlank(message = "Veillez indiquer votre nouveau mot de passe svp!")
    private String newPassword;
    @NotBlank(message = "Le mot de passe de confirmation est obligatoire")
    private String confirmPassword;
}
