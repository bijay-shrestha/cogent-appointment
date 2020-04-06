package com.cogent.cogentappointment.esewa.constraintvalidator.validator;

import com.cogent.cogentappointment.esewa.constraintvalidator.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.utils.commons.PatternUtils.isStatusActiveOrInactive;

/**
 * @author smriti on 2019-09-10
 */
public class StatusValidator implements ConstraintValidator<Status, Character> {

    @Override
    public boolean isValid(Character status, ConstraintValidatorContext context) {
        return !Objects.isNull(status) && isStatusActiveOrInactive(status);
    }
}
