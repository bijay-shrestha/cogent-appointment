package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.model.EmailToSend;

import java.util.Date;

/**
 * @author smriti on 2019-08-26
 */
public class EmailUtils {

    public static EmailToSend convertDTOToEmailToSend(EmailRequestDTO emailRequestDTO) {
        EmailToSend emailToSend = new EmailToSend();

        emailToSend.setReceiverEmailAddress(emailRequestDTO.getReceiverEmailAddress());
        emailToSend.setIsSent(StatusConstants.NO);
        emailToSend.setRecordedDate(new Date());
        emailToSend.setSubject(emailRequestDTO.getSubject());
        emailToSend.setTemplateName(emailRequestDTO.getTemplateName());
        emailToSend.setParamValue(emailRequestDTO.getParamValue());
        return emailToSend;
    }

    public static EmailToSend convertToUpdateEmailToSend(EmailToSend emailToSend) {
        emailToSend.setIsSent(StatusConstants.YES);
        emailToSend.setSentDate(new Date());
        return emailToSend;
    }
}
