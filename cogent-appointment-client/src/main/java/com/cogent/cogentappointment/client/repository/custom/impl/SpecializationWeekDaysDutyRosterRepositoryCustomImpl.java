package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.SpecializationWeekDaysDutyRosterRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@Repository
@Slf4j
@Transactional(readOnly = true)
public class SpecializationWeekDaysDutyRosterRepositoryCustomImpl implements SpecializationWeekDaysDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

}
