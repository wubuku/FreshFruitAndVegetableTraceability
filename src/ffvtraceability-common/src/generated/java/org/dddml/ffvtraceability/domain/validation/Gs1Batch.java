package org.dddml.ffvtraceability.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Gs1BatchValidator.class)
@Documented
public @interface Gs1Batch {
    String message() default "Invalid GS1 batch format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}