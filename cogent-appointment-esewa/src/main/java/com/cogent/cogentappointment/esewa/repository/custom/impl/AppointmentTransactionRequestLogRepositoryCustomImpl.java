package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentTransactionRequestLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.TRANSACTION_NUMBER;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.NAME;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.esewa.query.AppointmentTransactionRequestLogQuery.QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG;
import static com.cogent.cogentappointment.esewa.query.AppointmentTransactionRequestLogQuery.QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG_STATUS;

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

    @Override
    public Character fetchAppointmentTransactionStatus(String transactionNumber, String patientName) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_APPOINTMENT_TXN_REQUEST_LOG_STATUS)
                .setParameter(TRANSACTION_NUMBER, transactionNumber)
                .setParameter(NAME, patientName);

        try {
            return (Character) query.getSingleResult();
        } catch (NoResultException e) {
            return INACTIVE;
        }
    }
}
