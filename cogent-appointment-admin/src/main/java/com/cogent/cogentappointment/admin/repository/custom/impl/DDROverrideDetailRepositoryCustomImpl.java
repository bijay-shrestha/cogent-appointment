package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.DDRTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDROverrideDetailResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.DDROverrideDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.*;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDROverrideDetailQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeFromDateIn24HrFormat;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDROverrideDetailRepositoryCustomImpl implements DDROverrideDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<DDROverrideDetailResponseDTO> fetchDDROverrideDetail(Long ddrId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_DETAIL)
                .setParameter(DDR_ID, ddrId);

        return transformQueryToResultList(query, DDROverrideDetailResponseDTO.class);
    }

    @Override
    public List<DDRTimeResponseDTO> fetchDDROverrideTimeDetails(Date date, Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_TIME_DETAIL)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(DATE, utilDateToSqlDate(date));

        return transformQueryToResultList(query, DDRTimeResponseDTO.class);
    }

    @Override
    public List<DDRTimeResponseDTO> fetchDDROverrideTimeDetailsExceptCurrentId(Long ddrOverrideId,
                                                                               Date date, Long doctorId,
                                                                               Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_TIME_DETAIL_EXCEPT_CURRENT_ID)
                .setParameter(DDR_OVERRIDE_ID, ddrOverrideId)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(DATE, utilDateToSqlDate(date));

        return transformQueryToResultList(query, DDRTimeResponseDTO.class);
    }

    @Override
    public Long fetchDDROverrideCount(Long ddrId, Long ddrOverrideId,
                                      Date date, Date startTime, Date endTime) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT(ddrOverrideId))
                .setParameter(DATE, date)
                .setParameter(DDR_ID, ddrId)
                .setParameter(START_TIME, getTimeFromDateIn24HrFormat(startTime))
                .setParameter(END_TIME, getTimeFromDateIn24HrFormat(endTime));

        return (Long) (query.getSingleResult());
    }
}
