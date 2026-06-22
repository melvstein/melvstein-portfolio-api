package dev.melvstein.portfolio.api.common.validation.validator;

import dev.melvstein.portfolio.api.common.validation.annotation.EnumConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumConstraint, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumConstraint annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (value == null) {
            return true; // let @NotNull handle it
        }

        return acceptedValues.contains(value.toUpperCase());
    }
}
