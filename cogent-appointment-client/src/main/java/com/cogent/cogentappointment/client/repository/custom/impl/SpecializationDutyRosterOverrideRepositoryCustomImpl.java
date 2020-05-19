package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.SpecializationDutyRosterOverrideRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.SpecializationDutyRosterOverrideQuery.VALIDATE_SPECIALIZATION_DUTY_ROSTER_OVERRIDE_COUNT;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author Sauravi Thapa ON 5/18/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class SpecializationDutyRosterOverrideRepositoryCustomImpl implements
        SpecializationDutyRosterOverrideRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchOverrideCount(Long specializationId, Date fromDate, Date toDate) {
        Query query = createQuery.apply(entityManager, VALIDATE_SPECIALIZATION_DUTY_ROSTER_OVERRIDE_COUNT)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }
}
