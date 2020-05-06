package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.DOCTOR_DUTY_ROSTER_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.DOCTOR_ID;
import static com.cogent.cogentappointment.client.query.AppointmentTransferQuery.QUERY_TO_FETCH_DATES_BY_DOCTOR_ID;
import static com.cogent.cogentappointment.client.query.AppointmentTransferQuery.QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID;
import static com.cogent.cogentappointment.client.query.AppointmentTransferQuery.QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

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
    public List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId) {
        Query query=createQuery.apply(entityManager,QUERY_TO_FETCH_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID,doctorId);

        List<DoctorDatesResponseDTO> response=transformQueryToResultList(query,DoctorDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId) {
        Query query=createQuery.apply(entityManager,QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID,doctorDutyRosterId);

        List<String> response=query.getResultList();

        return response;
    }

    @Override
    public List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId) {
        Query query=createQuery.apply(entityManager,QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID,doctorId);

        List<DoctorDatesResponseDTO> response=transformQueryToResultList(query,DoctorDatesResponseDTO.class);

        return response;
    }
}
