package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDeptWeekDaysDutyRosterRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Slf4j
@Transactional(readOnly = true)
public class HospitalDeptWeekDaysDutyRosterRepositoryCustomImpl implements HospitalDeptWeekDaysDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

}
