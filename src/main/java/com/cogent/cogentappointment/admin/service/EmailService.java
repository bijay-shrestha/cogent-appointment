package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;

public interface EmailService {

    void sendEmail(EmailRequestDTO emailRequestDTO);

}
