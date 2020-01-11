package com.cogent.cogentappointment.constraintvalidator.validator;

import com.cogent.cogentappointment.constraintvalidator.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.cogent.cogentappointment.utils.commons.PatternUtils.isStatusActiveOrInactive;

/**
 * @author smriti on 2019-09-10
 */
public class StatusValidator implements ConstraintValidator<Status, Character> {

    @Override
    public boolean isValid(Character status, ConstraintValidatorContext context) {
        if (Objects.isNull(status)) return false;
        return isStatusActiveOrInactive(status);
    }
}
