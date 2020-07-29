package com.cogent.cogentappointment.esewa.repository.custom.impl;


import com.cogent.cogentappointment.esewa.repository.custom.AppointmentRefundDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * @author Sauravi Thapa २०/२/१०
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRefundDetailRepositoryCustomImpl implements AppointmentRefundDetailRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;
}

