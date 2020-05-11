package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.DDRShiftWiseQuery.VALIDATE_DDR_SHIFT_WISE_COUNT;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 08/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDRShiftWiseRepositoryCustomImpl implements DDRShiftWiseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Long validateDoctorDutyRosterCount(Long doctorId,
                                              Long specializationId,
                                              Date fromDate,
                                              Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DDR_SHIFT_WISE_COUNT)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }
}
