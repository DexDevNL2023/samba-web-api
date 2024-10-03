package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanRequest {
	@NotBlank(message = "L'email est obligatoire")
    private String email;
    private String password;
	@Pattern(regexp = "^data:image/(png|jpg|jpeg);base64,[A-Za-z0-9+/=]*$", message = "Format d'image non valide")
	private String imageUrl;
    private Boolean generatePassword = false;
}