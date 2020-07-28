package com.cogent.cogentappointment.admin.utils.commons;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.NAME_DUPLICATION_ERROR;
import static java.lang.reflect.Array.get;

/**
 * @author smriti ON 25/01/2020
 */
@Slf4j
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
                        String.format(NAME_AND_CODE_DUPLICATION_MESSAGE, className, requestedName, requestedCode),
                        "name", requestedName, "code", requestedCode
                );

            validateName(isNameExists, requestedName, className);
            validateCode(isCodeExists, requestedCode, className);
        });
    }

    public static void validateNepaliNameDuplicity(List<Object[]> objects,
                                                   String requestedNepaliName,
                                                   String className) {
        final int NEPALI_NAME = 2;

        objects.forEach(object -> {

            boolean isNameExists = requestedNepaliName.equalsIgnoreCase((String) get(object, NEPALI_NAME));

            validateNepaliName(isNameExists, requestedNepaliName, className);

        });
    }

    private static void validateNepaliName(boolean isNameExists, String name, String className) {
        if (isNameExists)
            throw new DataDuplicationException(
                    String.format(NEPALI_NAME_DUPLICATION_MESSAGE, className, name),
                    "name", name);
    }

    private static void validateName(boolean isNameExists, String name, String className) {
        if (isNameExists){
            log.error(NAME_DUPLICATION_ERROR, className, name);
            throw new DataDuplicationException(String.format(NAME_DUPLICATION_MESSAGE, className, name), "name", name);
        }
    }

    private static void validateCode(boolean isCodeExists, String code, String className) {
        if (isCodeExists){
            log.error(NAME_DUPLICATION_ERROR, className, code);
            throw new DataDuplicationException(String.format(CODE_DUPLICATION_MESSAGE, className, code), "code", code);
        }
    }
}
