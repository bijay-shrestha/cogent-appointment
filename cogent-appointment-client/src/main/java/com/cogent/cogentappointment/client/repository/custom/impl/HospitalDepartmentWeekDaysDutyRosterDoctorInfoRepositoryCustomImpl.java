package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAndWeekdaysDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAndDoctorDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentWeekDaysDutyRosterDoctorInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPT_AND_DOCTOR_LIST;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 07/06/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustomImpl
        implements HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final MinIOProperties minIOProperties;

    public HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustomImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public HospitalDeptAndDoctorDTO fetchHospitalDeptAndDoctorInfo(HospitalDeptAndWeekdaysDTO hospitalDeptAndWeekdaysDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPT_AND_DOCTOR_LIST)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDeptAndWeekdaysDTO.getHospitalDepartmentId())
                .setParameter(WEEK_DAY_NAME, hospitalDeptAndWeekdaysDTO.getWeekDay())
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        return HospitalDeptAndDoctorDTO.builder()
                .doctorInfo(results)
                .hospitalDepartmentId(hospitalDeptAndWeekdaysDTO.getHospitalDepartmentId())
                .build();
    }
}
