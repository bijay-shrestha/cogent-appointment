package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.HospitalDeptDutyRosterRoomInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_ROOM_COUNT;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_ROOM_COUNT_EXCEPT_CURRENT_ID;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 21/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRoomInfoRepositoryCustomImpl implements HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchRoomCount(Long hospitalDeptId, Date fromDate, Date toDate, Long roomId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROOM_COUNT)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(ROOM_ID, roomId)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDeptId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchRoomCountExceptCurrentId(Long hospitalDeptId, Date fromDate, Date toDate, Long roomId,
                                              Long hddRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROOM_COUNT_EXCEPT_CURRENT_ID)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(ROOM_ID, roomId)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDeptId)
                .setParameter(ID, hddRosterId);

        return (Long) query.getSingleResult();
    }
}
