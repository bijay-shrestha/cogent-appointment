package com.cogent.cogentappointment.admin.constraintvalidator.validator;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.utils.commons.PatternUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author smriti on 2019-09-10
 */
public class StatusValidator implements ConstraintValidator<Status, Character> {

    @Override
    public boolean isValid(Character status, ConstraintValidatorContext context) {
        if (Objects.isNull(status)) return false;
        return PatternUtils.isStatusActiveOrInactive(status);
    }
}
