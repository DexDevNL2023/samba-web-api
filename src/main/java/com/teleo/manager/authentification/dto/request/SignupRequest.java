package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRequest {
    private String fullName;
    private String login;
	@NotBlank(message = "L'email est obligatoire")
    private String email;
    private String password;
	private Boolean usingQr=false;
	@Pattern(regexp = "^data:image/(png|jpg|jpeg);base64,[A-Za-z0-9+/=]*$", message = "Format d'image non valide")
	private String imageUrl;
    private Boolean generatePassword = false;

	public static Builder getBuilder() {
		return new Builder();
	}

	public static class Builder {
		private String fullName;
		private String login;
		private String email;
		private String password;
		private Boolean usingQr=false;
		private String imageUrl;
		private Boolean generatePassword = false;

		public Builder addFullName(final String fullName) {
			this.fullName = fullName;
			return this;
		}

		public Builder addLogin(final String login) {
			this.login = login;
			return this;
		}

		public Builder addEmail(final String email) {
			this.email = email;
			return this;
		}

		public Builder addPassword(final String password) {
			this.password = password;
			return this;
		}

		public Builder addPassword(final Boolean usingQr) {
			this.usingQr = usingQr;
			return this;
		}

		public Builder addImageUrl(final String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public Builder addGeneratePassword(final Boolean generatePassword) {
			this.generatePassword = generatePassword;
			return this;
		}

		public SignupRequest build() {
			return new SignupRequest(fullName, login, email, password, usingQr, imageUrl, generatePassword);
		}
	}
}