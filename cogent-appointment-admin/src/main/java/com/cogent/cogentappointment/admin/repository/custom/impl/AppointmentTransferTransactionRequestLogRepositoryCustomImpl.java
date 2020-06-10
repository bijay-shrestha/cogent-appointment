package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransferTransactionRequestLogRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sauravi Thapa ON 5/10/20
 */

@Repository
@Slf4j
@Transactional(readOnly = true)
public class AppointmentTransferTransactionRequestLogRepositoryCustomImpl implements
        AppointmentTransferTransactionRequestLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


}
