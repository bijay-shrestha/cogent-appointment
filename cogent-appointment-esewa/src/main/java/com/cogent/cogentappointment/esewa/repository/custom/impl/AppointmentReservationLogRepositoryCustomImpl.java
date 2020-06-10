package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentReservationLogRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.query.AppointmentReservationLogQuery.QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.query.AppointmentReservationLogQuery.QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG_ID;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 19/02/20
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentReservationLogRepositoryCustomImpl implements AppointmentReservationLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> fetchBookedAppointmentReservations(AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG)
                .setParameter(DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return query.getResultList();
    }

    @Override
    public Long fetchAppointmentReservationLogId(Date appointmentDate, String appointmentTime,
                                                 Long doctorId, Long specializationId, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_RESERVATION_LOG_ID)
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(APPOINTMENT_TIME, appointmentTime)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
