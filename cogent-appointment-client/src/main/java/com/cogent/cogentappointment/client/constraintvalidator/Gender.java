package com.cogent.cogentappointment.client.constraintvalidator;

import com.cogent.cogentappointment.client.constraintvalidator.validator.GenderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {GenderValidator.class})
public @interface Gender {

    String message() default "must be M/F/O";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
