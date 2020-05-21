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
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterOverrideQuery.QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterOverrideQuery.QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM;
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
    public Long fetchOverrideCountWithoutRoom(Long hospitalDepartmentId, Date fromDate, Date toDate) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchOverrideCountWithRoom(Long hospitalDepartmentId, Date fromDate,
                                           Date toDate, Long roomId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(ROOM_ID, roomId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }
}
