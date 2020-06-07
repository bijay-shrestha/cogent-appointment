package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.HospitalBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalBillingModeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
@Repository
@Slf4j
@Transactional(readOnly = true)
public class HospitalBillingModeInfoRepositoryCustomImpl implements HospitalBillingModeInfoRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

}
