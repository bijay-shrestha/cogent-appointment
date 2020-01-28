package com.cogent.cogentappointment.admin.constraintvalidator.validator;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.utils.commons.PatternUtils.hasSpecialCharacter;

/**
 * @author smriti on 2019-09-15
 */
public class SpecialCharactersValidator implements ConstraintValidator<SpecialCharacters, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Objects.isNull(value) || !hasSpecialCharacter(value);
    }
}
