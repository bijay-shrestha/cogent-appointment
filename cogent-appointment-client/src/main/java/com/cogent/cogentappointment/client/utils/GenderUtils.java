package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.persistence.enums.Gender;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_GENDER_CODE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_GENDER_CODE_MESSAGE;

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
                throw new BadRequestException(INVALID_GENDER_CODE_MESSAGE, INVALID_GENDER_CODE_DEBUG_MESSAGE);
        }
    }
}
