package com.cogent.cogentappointment.client.constraintvalidator.validator;

import com.cogent.cogentappointment.client.constraintvalidator.Gender;
import com.cogent.cogentappointment.client.constraintvalidator.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author smriti on 2019-09-10
 */
public class GenderValidator implements ConstraintValidator<Gender, Character> {

    @Override
    public boolean isValid(Character gender, ConstraintValidatorContext context) {
        return !Objects.isNull(gender) &&
                (gender.equals('M') || gender.equals('F') || gender.equals('O'));
    }
}
