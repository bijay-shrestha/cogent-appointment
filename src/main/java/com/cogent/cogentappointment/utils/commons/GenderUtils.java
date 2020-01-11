package com.cogent.cogentappointment.utils.commons;

import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.exception.OperationUnsuccessfulException;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.INVALID_GENDER_CODE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.constants.ErrorMessageConstants.INVALID_GENDER_CODE_MESSAGE;
import static com.cogent.cogentappointment.constants.GenderConstants.*;

/**
 * @author smriti ON 11/01/2020
 */
public class GenderUtils {

    public static Gender fetchGender(String code) {

        switch (code) {
            case MALE:
                return Gender.MALE;
            case FEMALE:
                return Gender.FEMALE;
            case OTHERS:
                return Gender.OTHERS;
            default:
                throw new OperationUnsuccessfulException(INVALID_GENDER_CODE_MESSAGE, INVALID_GENDER_CODE_DEBUG_MESSAGE);
        }
    }
}
