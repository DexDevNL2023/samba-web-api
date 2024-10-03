package com.teleo.manager.generic.validators.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.teleo.manager.authentification.dto.request.ChangePasswordRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ChangePasswordRequest> {

	@Override
    public boolean isValid(final ChangePasswordRequest user, final ConstraintValidatorContext context) {
        return user.getNewPassword().equals(user.getConfirmPassword());
	}
}
