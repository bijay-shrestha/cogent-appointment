package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransactionRequestLogRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * @author smriti on 31/03/20
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentTransactionRequestLogRepositoryCustomImpl implements AppointmentTransactionRequestLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
}
