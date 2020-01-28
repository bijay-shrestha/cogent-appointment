package com.cogent.cogentappointment.admin.utils.commons;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;

import java.util.List;

import static java.lang.reflect.Array.get;

/**
 * @author smriti ON 25/01/2020
 */
public class NameAndCodeValidationUtils {

    public static void validateDuplicity(List<Object[]> objects,
                                         String requestedName,
                                         String requestedCode,
                                         String className) {
        final int NAME = 0;
        final int CODE = 1;

        objects.forEach(object -> {
            boolean isNameExists = requestedName.equalsIgnoreCase((String) get(object, NAME));

            boolean isCodeExists = requestedCode.equalsIgnoreCase((String) get(object, CODE));

            if (isNameExists && isCodeExists)
                throw new DataDuplicationException(
                        String.format(ErrorMessageConstants.NAME_AND_CODE_DUPLICATION_MESSAGE, className, requestedName, requestedCode),
                        "name", requestedName, "code", requestedCode
                );

            validateName(isNameExists, requestedName, className);
            validateCode(isCodeExists, requestedCode, className);
        });
    }

    private static void validateName(boolean isNameExists, String name, String className) {
        if (isNameExists)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.NAME_DUPLICATION_MESSAGE, className, name),
                    "name", name);
    }

    private static void validateCode(boolean isCodeExists, String code, String className) {
        if (isCodeExists)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.CODE_DUPLICATION_MESSAGE, className, code),
                    "code", code);
    }
}
