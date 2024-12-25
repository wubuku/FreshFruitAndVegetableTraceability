package org.dddml.ffvtraceability.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class Gs1BatchValidator implements ConstraintValidator<Gs1Batch, String> {
    private static final Pattern GS1_BATCH_PATTERN = Pattern.compile("^[A-Z0-9/\\-._]{1,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || GS1_BATCH_PATTERN.matcher(value).matches();
    }
}