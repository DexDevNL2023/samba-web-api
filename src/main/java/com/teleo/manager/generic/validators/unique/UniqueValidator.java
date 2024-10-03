package com.teleo.manager.generic.validators.unique;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, TYPE, TYPE_USE, TYPE_PARAMETER, METHOD, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidatorConstraint.class)
@NotNull
@ReportAsSingleViolation
public @interface UniqueValidator {
    Class<? extends FieldValueExists> service();

    String serviceQualifier() default "";

    String fieldName();

    String message() default "Le champ n'est pas unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
