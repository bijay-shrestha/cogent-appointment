package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterQuery.QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_INFO;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRepositoryCustomImpl implements HospitalDeptDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Date date, Long hospitalDepartmentId) {

        List<HospitalDepartmentDutyRoster> hospitalDepartmentDutyRoster =
                entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_INFO, HospitalDepartmentDutyRoster.class)
                        .setParameter(DATE, utilDateToSqlDate(date))
                        .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                        .getResultList();

        if (ObjectUtils.isEmpty(hospitalDepartmentDutyRoster))
            throw HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND.get();

        return hospitalDepartmentDutyRoster;
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class);
    };

}
