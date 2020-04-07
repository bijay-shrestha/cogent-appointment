package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE;
import static com.cogent.cogentappointment.esewa.query.DoctorDutyRosterOverrideQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME;
import static com.cogent.cogentappointment.esewa.query.EsewaQuery.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentDetailsUtils.parseToDoctorAvailabilityStatusResponseDTO;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;

@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorDutyRosterOverrideRepositoryCustomImpl implements DoctorDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                             Long doctorId,
                                                                             Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME)
                .setParameter(DATE, utilDateToSqlDate(date))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDutyRosterTimeResponseDTO> responseDTOList =
                transformQueryToResultList(query, DoctorDutyRosterTimeResponseDTO.class);

        return responseDTOList.isEmpty() ? null : responseDTOList.get(0);
    }

    @Override
    public List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DUTY_ROSTER_OVERRIDE_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        try {
            return transformQueryToResultList(query, DoctorDutyRosterOverrideAppointmentDate.class);
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, DoctorDutyRosterOverrideAppointmentDate.class.getSimpleName());
            throw new NoContentFoundException("Not Found");
        }
    }

    @Override
    public DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterOverrideStatus(AppointmentDetailRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO))
                .setParameter(DATE, requestDTO.getDate())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Objects[]> results = query.getResultList();

        return results.isEmpty() ? null : parseToDoctorAvailabilityStatusResponseDTO(results.get(0));
    }

    @Override
    public List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR_OVERRIDE(requestDTO))
                .setParameter(DATE, requestDTO.getDate())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AvailableDoctorWithSpecialization.class);
    }

    @Override
    public List<DutyRosterOverrideAppointmentDate> fetchDayOffRosterOverridebyRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_DAY_OFF_ROSTER_OVERRIDE_DATES)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DutyRosterOverrideAppointmentDate.class);
    }

    private NoContentFoundException DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER_OVERRIDE);
        throw new NoContentFoundException(DoctorDutyRosterOverride.class);
    }
}
