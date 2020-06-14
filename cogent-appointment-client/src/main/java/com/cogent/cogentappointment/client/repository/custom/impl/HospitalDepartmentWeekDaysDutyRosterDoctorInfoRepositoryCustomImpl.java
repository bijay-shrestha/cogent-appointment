package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAndWeekdaysDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAndDoctorDTO;
import com.cogent.cogentappointment. client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment. client.exception.NoContentFoundException;
import com.cogent.cogentappointment. client.repository.custom.HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment. client.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment. client.constants.QueryConstants.WEEK_DAY_NAME;
import static com.cogent.cogentappointment. client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment. client.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment. client.query.HospitalDepartmentWeekDaysDutyRosterDoctorInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPT_AND_DOCTOR_LIST;
import static com.cogent.cogentappointment. client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment. client.utils.commons.QueryUtils.transformQueryToResultList;

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


    @Override
    public HospitalDeptAndDoctorDTO fetchHospitalDeptAndDoctorInfo(HospitalDeptAndWeekdaysDTO hospitalDeptAndWeekdaysDTO) {

        Query query=createQuery.apply(entityManager,QUERY_TO_FETCH_HOSPITAL_DEPT_AND_DOCTOR_LIST)
                .setParameter(HOSPITAL_DEPARTMENT_ID,hospitalDeptAndWeekdaysDTO.getHospitalDepartmentId())
                .setParameter(WEEK_DAY_NAME,hospitalDeptAndWeekdaysDTO.getWeekDay());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()) {
            throw DOCTOR_NOT_FOUND.get();
        }else{
            return HospitalDeptAndDoctorDTO.builder()
                    .doctorInfo(results)
                    .hospitalDepartmentId(hospitalDeptAndWeekdaysDTO.getHospitalDepartmentId())
                    .build();
        }


    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR);
        throw new NoContentFoundException(Doctor.class);
    };
}
