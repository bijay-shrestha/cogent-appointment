package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.HospitalDeptDutyRosterOverrideRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HopsitalDeptDutyRosterOverrideQuery.QUERY_TO_FETCH_DDR_OVERRIDE_COUNT;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterOverrideRepositoryCustomImpl implements
        HospitalDeptDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchOverrideCount(Long hospitalDepartmentId, Date fromDate, Date toDate) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT)
                .setParameter(HOSPITAL_DEPARMTENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }
}
