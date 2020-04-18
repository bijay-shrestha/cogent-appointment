package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.persistence.model.EmailToSend;

import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;

/**
 * @author smriti on 2019-08-26
 */
public class EmailUtils {

    public static EmailToSend convertDTOToEmailToSend(EmailRequestDTO emailRequestDTO) {
        EmailToSend emailToSend = new EmailToSend();

        emailToSend.setReceiverEmailAddress(emailRequestDTO.getReceiverEmailAddress());
        emailToSend.setIsSent(NO);
        emailToSend.setRecordedDate(new Date());
        emailToSend.setSubject(emailRequestDTO.getSubject());
        emailToSend.setTemplateName(emailRequestDTO.getTemplateName());
        emailToSend.setParamValue(emailRequestDTO.getParamValue());
        return emailToSend;
    }

    public static void updateEmailToSendStatus(EmailToSend emailToSend) {
        emailToSend.setIsSent(YES);
        emailToSend.setSentDate(new Date());
    }
}
