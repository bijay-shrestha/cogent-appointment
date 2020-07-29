package com.cogent.cogentappointment.esewa.constraintvalidator;

import com.cogent.cogentappointment.esewa.constraintvalidator.validator.StatusValidator;

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
@Constraint(validatedBy = {StatusValidator.class})
public @interface Status {

    String message() default "must be Active/Inactive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
