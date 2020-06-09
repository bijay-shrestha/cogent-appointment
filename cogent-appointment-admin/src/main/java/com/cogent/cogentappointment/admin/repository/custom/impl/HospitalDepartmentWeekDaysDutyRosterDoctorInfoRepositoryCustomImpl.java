package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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


}
