package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.persistence.model.EmailToSend;

public interface EmailService {

    EmailToSend saveEmailToSend(EmailRequestDTO emailRequestDTO);

    void sendEmail(EmailRequestDTO requestDTO);

    void sendEmail();

}
