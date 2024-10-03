package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {
    @NotBlank(message = "Veillez renseigner votre token svp!")
    private String token;
    @NotBlank(message = "Veillez indiquer votre nouveau mot de passe svp!")
    private String newPassword;
}
