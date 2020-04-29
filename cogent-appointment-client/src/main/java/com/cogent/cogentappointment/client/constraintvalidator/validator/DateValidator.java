package com.cogent.cogentappointment.client.constraintvalidator.validator;

import com.cogent.cogentappointment.client.constraintvalidator.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.cogent.cogentappointment.client.utils.commons.PatternUtils.isDateFormatValid;

/**
 * @author smriti on 2019-09-15
 */
public class DateValidator implements ConstraintValidator<DateFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || !isDateFormatValid(value);
    }
}
