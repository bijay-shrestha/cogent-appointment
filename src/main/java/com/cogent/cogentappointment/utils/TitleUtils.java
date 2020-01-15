package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.enums.Title;
import com.cogent.cogentappointment.exception.BadRequestException;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.INVALID_TITLE_CODE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.constants.ErrorMessageConstants.INVALID_TITLE_CODE_MESSAGE;

/**
 * @author smriti ON 14/01/2020
 */
public class TitleUtils {
    public static Title fetchTitleByCode(String code) {

        switch (code) {
            case "Mr.":
                return Title.MR;
            case "Ms.":
                return Title.MS;
            case "Mrs.":
                return Title.MRS;
            case "Mx.":
                return Title.MX;
            default:
                throw new BadRequestException(INVALID_TITLE_CODE_MESSAGE, INVALID_TITLE_CODE_DEBUG_MESSAGE);
        }
    }
}
