package com.cogent.cogentthirdpartyconnector.exception.utils;


import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.commons.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentthirdpartyconnector.constants.HMACConstant.SPACE;
import static com.cogent.cogentthirdpartyconnector.utils.StringUtil.splitByCharacterTypeCamelCase;
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
}
