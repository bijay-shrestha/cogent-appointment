package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentTransferRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransfer;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransferLog.APPOINTMENT_TRANSFER;
import static com.cogent.cogentappointment.admin.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.admin.query.AppointmentTransferQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@Repository
@Slf4j
@Transactional(readOnly = true)
public class AppointmentTransferRepositoryCustomImpl implements AppointmentTransferRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId, Long specializationId,Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDatesResponseDTO> response = transformQueryToResultList(query, DoctorDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        List<String> response = query.getResultList();

        return response;
    }

    @Override
    public WeekDayAndTimeDTO getWeekDaysByCode(Long doctorId, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(CODE, code);

        try {
            return transformQueryToSingleResult(query, WeekDayAndTimeDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }

    }

    @Override
    public List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId, Long specializationId,Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDatesResponseDTO> response = transformQueryToResultList(
                query, DoctorDatesResponseDTO.class);

        return response;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
    };

    private Supplier<NoContentFoundException> APPOINTMENT_TRANSFERE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_TRANSFER);
        throw new NoContentFoundException(AppointmentTransfer.class);
    };
}
