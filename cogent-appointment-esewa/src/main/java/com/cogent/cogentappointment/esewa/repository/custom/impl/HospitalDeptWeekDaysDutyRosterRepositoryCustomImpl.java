package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptWeekDaysDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.CODE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterWeekDaysQuery.QUERY_TO_FETCH_HOSPITAL_DEPT_WEEK_DAYS_INFO;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDayCodeFromDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author smriti on 31/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptWeekDaysDutyRosterRepositoryCustomImpl implements HospitalDeptWeekDaysDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HospitalDeptDutyRosterTimeResponseTO fetchWeekDaysTimeInfo(Long hddRosterId, Date date) {
        Date sqlDate = utilDateToSqlDate(date);

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPT_WEEK_DAYS_INFO)
                .setParameter(ID, hddRosterId)
                .setParameter(CODE, getDayCodeFromDate(sqlDate));

        try {
            return transformQueryToSingleResult(query, HospitalDeptDutyRosterTimeResponseTO.class);
        } catch (NoResultException e) {
            throw HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND.get();
        }
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class);
    };

}