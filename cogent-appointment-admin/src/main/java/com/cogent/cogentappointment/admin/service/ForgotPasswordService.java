package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.forgotPassword.ForgotPasswordRequestDTO;

/**
 * @author smriti on 2019-09-20
 */
public interface ForgotPasswordService {
    void forgotPassword(String email);

    void verify(String resetCode);

    void updatePassword(ForgotPasswordRequestDTO requestDTO);
}
