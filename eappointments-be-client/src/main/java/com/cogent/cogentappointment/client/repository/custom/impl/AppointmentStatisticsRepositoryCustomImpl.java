package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.AppointmentStatisticsRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentStatisticsRepositoryCustomImpl implements AppointmentStatisticsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

}
