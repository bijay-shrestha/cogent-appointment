package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.esewa.utils.AppointmentUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.esewa.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;


/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                            Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_APPOINTMENT_EXISTS)
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(APPOINTMENT_TIME, appointmentTime);

        return (Long) query.getSingleResult();
    }

    /*USED IN APPOINTMENT CHECK AVAILABILITY*/
    @Override
    public List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate,
                                            Long hospitalId) {

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(FROM_DATE, fetchStartingFiscalYear(year, month))
                .setParameter(TO_DATE, fetchEndingFiscalYear(year, month))
                .setParameter(HOSPITAL_ID, hospitalId);

        return AppointmentUtils.generateAppointmentNumber(query.getResultList());
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> pendingAppointments =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (pendingAppointments.isEmpty()) {
            throw APPOINTMENT_NOT_FOUND();
        }
        return pendingAppointments;
    }


    @Override
    public AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAILS_BY_ID)
                .setParameter(ID, appointmentId);

        List<AppointmentDetailResponseDTO> appointmentDetails =
                transformQueryToResultList(query, AppointmentDetailResponseDTO.class);

        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);

        return appointmentDetails.get(0);
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_HISTORY)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> appointmentHistory =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (appointmentHistory.isEmpty())
            throw APPOINTMENT_NOT_FOUND();


        return appointmentHistory;
    }

    @Override
    public Double calculateRefundAmount(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT)
                .setParameter(ID, appointmentId);

        try {
            return Double.parseDouble(query.getSingleResult().toString());
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }


    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    public NoContentFoundException APPOINTMENT_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
        throw new NoContentFoundException(Appointment.class);
    }


}
