package com.cogent.cogentappointment.client.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-09-20
 */
@Repository
@Qualifier("forgotPasswordRepositoryCustom")
public interface ForgotPasswordRepositoryCustom {

    Object findByResetCode(String resetCode);
}
