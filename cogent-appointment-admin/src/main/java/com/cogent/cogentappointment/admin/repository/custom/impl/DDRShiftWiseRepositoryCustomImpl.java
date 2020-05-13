package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinDTO;
import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRShiftWiseQuery.QUERY_TO_FETCH_EXISTING_DDR;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRShiftWiseQuery.VALIDATE_DDR_SHIFT_WISE_COUNT;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

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

    @Override
    public List<DDRExistingMinDTO> fetchExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_DDR)
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId())
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        return transformQueryToResultList(query, DDRExistingMinDTO.class);
    }
}
