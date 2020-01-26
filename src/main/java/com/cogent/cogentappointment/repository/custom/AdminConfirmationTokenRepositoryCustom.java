package com.cogent.cogentappointment.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-09-23
 */
@Repository
@Qualifier("adminConfirmationTokenRepositoryCustom")
public interface AdminConfirmationTokenRepositoryCustom {
    Object findByConfirmationToken(String token);
}
