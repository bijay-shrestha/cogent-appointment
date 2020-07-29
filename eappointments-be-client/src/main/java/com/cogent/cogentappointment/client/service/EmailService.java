package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.persistence.model.EmailToSend;

public interface EmailService {

    EmailToSend saveEmailToSend(EmailRequestDTO emailRequestDTO);

    void sendEmail(EmailRequestDTO emailRequestDTO);

}
