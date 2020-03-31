package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionRequestLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.TRANSACTION_NUMBER;
import static com.cogent.cogentappointment.client.constants.QueryConstants.NAME;
import static com.cogent.cogentappointment.client.query.AppointmentTransactionRequestLogQuery.QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG;

/**
 * @author smriti on 31/03/20
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentTransactionRequestLogRepositoryCustomImpl implements AppointmentTransactionRequestLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppointmentTransactionRequestLog fetchAppointmentTransactionRequestLog(String transactionNumber,
                                                                                  String patientName) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG,
                    AppointmentTransactionRequestLog.class)
                    .setParameter(TRANSACTION_NUMBER, transactionNumber)
                    .setParameter(NAME, patientName)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
