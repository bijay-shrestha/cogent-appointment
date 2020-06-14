package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.HospitalDepartmentDoctorInfoResponseDTO;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.CODE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_DEPARTMENT_DUTY_ROSTER_ID;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentWeekDaysDutyRosterDoctorInfoQuery.QUERY_TO_FETCH_AVAILABLE_DOCTORS;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 10/06/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustomImpl implements
        HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalDepartmentDoctorInfoResponseDTO> fetchAvailableDoctors(Long hddRosterId, String weekDayCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_DOCTORS)
                .setParameter(HOSPITAL_DEPARTMENT_DUTY_ROSTER_ID, hddRosterId)
                .setParameter(CODE, weekDayCode);

        return transformQueryToResultList(query, HospitalDepartmentDoctorInfoResponseDTO.class);
    }
}
