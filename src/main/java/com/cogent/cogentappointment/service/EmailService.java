package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.request.email.EmailRequestDTO;

public interface EmailService {

    void sendEmail(EmailRequestDTO emailRequestDTO);

}
