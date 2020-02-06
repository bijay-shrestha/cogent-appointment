package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.constants.StringConstant;
import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.EmailConstants.SUBJECT_FOR_FORGOT_PASSWORD;
import static com.cogent.cogentappointment.client.constants.EmailTemplates.FORGOT_PASSWORD;

/**
 * @author smriti on 2019-09-20
 */
public class ForgotPasswordUtils {

    public static ForgotPasswordVerification convertToForgotPasswordVerification(
            Admin admin,
            int expirationTime,
            ForgotPasswordVerification verification) {

        verification.setResetCode(NumberFormatterUtils.generateRandomNumber(7));
        verification.setExpirationDate(calculateExpirationDate(expirationTime));
        verification.setAdmin(admin);
        verification.setStatus(StatusConstants.ACTIVE);
        return verification;
    }

    public static EmailRequestDTO parseToEmailRequestDTO(String emailAddress,
                                                         String username,
                                                         String resetCode) {
        return EmailRequestDTO.builder()
                .receiverEmailAddress(emailAddress)
                .subject(SUBJECT_FOR_FORGOT_PASSWORD)
                .templateName(FORGOT_PASSWORD)
                .paramValue(username + StringConstant.COMMA_SEPARATED + resetCode)
                .build();
    }

    private static Date calculateExpirationDate(int expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }
}
