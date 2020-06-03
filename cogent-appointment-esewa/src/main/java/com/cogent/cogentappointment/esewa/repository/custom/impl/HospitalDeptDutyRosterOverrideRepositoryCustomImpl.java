package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterOverrideRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterOverrideQuery.QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_TIME;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterOverrideRepositoryCustomImpl implements HospitalDeptDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRosterOverrideTimeInfo(Long hddRosterId,
                                                                                            Date date,
                                                                                            Long hospitalDepartmentId,
                                                                                            Long hospitalDepartmentRoomInfoId) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_TIME(hospitalDepartmentRoomInfoId))
                .setParameter(DATE, utilDateToSqlDate(date))
                .setParameter(ID, hddRosterId)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        try {
            return transformQueryToSingleResult(query, HospitalDeptDutyRosterTimeResponseTO.class);
        } catch (NoResultException e) {
            return null;
        }
    }
}
