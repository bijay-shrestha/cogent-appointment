package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;

public interface EmailService {

    void sendEmail(EmailRequestDTO emailRequestDTO);

}
