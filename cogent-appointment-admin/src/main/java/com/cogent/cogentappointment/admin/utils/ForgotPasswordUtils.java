package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;

import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.SUBJECT_FOR_FORGOT_PASSWORD;
import static com.cogent.cogentappointment.admin.constants.EmailTemplates.FORGOT_PASSWORD;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.generateRandomNumber;

/**
 * @author smriti on 2019-09-20
 */
public class ForgotPasswordUtils {

    public static ForgotPasswordVerification convertToForgotPasswordVerification(
            Admin admin,
            int expirationTime,
            ForgotPasswordVerification verification) {

        verification.setResetCode(generateRandomNumber(6));
        verification.setExpirationDate(calculateExpirationDate(expirationTime));
        verification.setAdmin(admin);
        verification.setStatus(ACTIVE);
        return verification;
    }

    public static EmailRequestDTO parseToEmailRequestDTO(String emailAddress,
                                                         String fullname,
                                                         String resetCode) {
        return EmailRequestDTO.builder()
                .receiverEmailAddress(emailAddress)
                .subject(SUBJECT_FOR_FORGOT_PASSWORD)
                .templateName(FORGOT_PASSWORD)
                .paramValue(fullname + COMMA_SEPARATED + resetCode)
                .build();
    }

    private static Date calculateExpirationDate(int expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }
}
