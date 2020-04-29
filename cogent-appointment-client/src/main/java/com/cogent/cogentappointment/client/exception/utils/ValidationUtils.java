package com.cogent.cogentappointment.client.exception.utils;

import com.cogent.cogentappointment.client.exception.ConstraintViolationException;
import com.cogent.cogentappointment.client.utils.commons.StringUtil;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.constants.StringConstant.SPACE;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.*;
import static org.springframework.util.StringUtils.capitalize;

/**
 * @author smriti on 7/2/19
 */
public class ValidationUtils {

    public static String getExceptionForMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        List<String> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                            return capitalize(splitByCharacterTypeCamelCase(
                                    error.getField())) + SPACE + error.getDefaultMessage();
                        }
                ).collect(Collectors.toList());

        return String.join(COMMA_SEPARATED + SPACE, violations);
    }

    /*
    ConstraintViolationException to be thrown when validation on an argument annotated with {@code @Valid}
   (request dtos) fails on Service layer
  */
    public static <T> void validateConstraintViolation(Set<ConstraintViolation<T>> constraintViolations) {
        if (!constraintViolations.isEmpty()) {
            List<String> violations = constraintViolations.stream().map(
                    violation -> {
                        return capitalize(splitByCharacterTypeCamelCase(
                                violation.getPropertyPath().toString())) + SPACE + violation.getMessage();
                    }
            ).collect(Collectors.toList());

            String errorMessages = String.join(COMMA_SEPARATED + SPACE, violations);

            throw new ConstraintViolationException(errorMessages, errorMessages);
        }
    }
}
