package com.Aditya.Dispatch.Load.Balancer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CoordinatesValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCoordinates {
    String message() default "Latitude must be between -90 and 90, Longitude between -180 and 180";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
