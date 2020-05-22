package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentChargeRepositoryCustom;
import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentDoctorInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentDoctorInfoRepositoryCustomImpl implements HospitalDepartmentDoctorInfoRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

}
