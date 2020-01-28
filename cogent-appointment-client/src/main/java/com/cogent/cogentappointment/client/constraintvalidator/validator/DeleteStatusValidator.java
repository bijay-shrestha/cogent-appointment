package com.cogent.cogentappointment.client.constraintvalidator.validator;

import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.constraintvalidator.DeleteStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author smriti on 2019-09-10
 */
public class DeleteStatusValidator implements ConstraintValidator<DeleteStatus, Character> {
    @Override
    public boolean isValid(Character status, ConstraintValidatorContext context) {
        if (Objects.isNull(status)) return false;
        return status.equals(StatusConstants.DELETED);
    }
}
