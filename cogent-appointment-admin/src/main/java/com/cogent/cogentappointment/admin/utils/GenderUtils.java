package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.enums.Gender;
import com.cogent.cogentappointment.admin.exception.BadRequestException;

/**
 * @author smriti ON 11/01/2020
 */
public class GenderUtils {

    public static Gender fetchGenderByCode(Character code) {

        switch (code) {
            case 'M':
                return Gender.MALE;
            case 'F':
                return Gender.FEMALE;
            case 'O':
                return Gender.OTHERS;
            default:
                throw new BadRequestException(ErrorMessageConstants.INVALID_GENDER_CODE_MESSAGE, ErrorMessageConstants.INVALID_GENDER_CODE_DEBUG_MESSAGE);
        }
    }
}
