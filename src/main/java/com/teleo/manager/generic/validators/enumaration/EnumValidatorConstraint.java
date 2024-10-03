package com.teleo.manager.generic.validators.enumaration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidatorConstraint implements ConstraintValidator<EnumValidator, Enum<?>> {

    List<String> values = new ArrayList<>();

    @Override
    public void initialize(EnumValidator enumValue) {
        values = Stream.of(enumValue.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return values.contains(value.name());
    }
}
