package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.EmailConstants;
import com.cogent.cogentappointment.admin.constants.EmailTemplates;
import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.model.Admin;
import com.cogent.cogentappointment.admin.model.ForgotPasswordVerification;
import com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils;

import java.util.Date;

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
                .subject(EmailConstants.SUBJECT_FOR_FORGOT_PASSWORD)
                .templateName(EmailTemplates.FORGOT_PASSWORD)
                .paramValue(username + StringConstant.COMMA_SEPARATED + resetCode)
                .build();
    }

    private static Date calculateExpirationDate(int expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }
}
