package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferTransactionRepositoryCustom;
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
public class AppointmentTransferTransactionRepositoryCustomImpl implements AppointmentTransferTransactionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


}
