package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRBreakResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DDRBreakDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRBreakDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_WEEK_DAYS_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRBreakDetailQuery.QUERY_TO_FETCH_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRBreakDetailQuery.QUERY_TO_FETCH_BREAK_DETAIL_FOR_UPDATE_MODAL;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 14/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDRBreakDetailRepositoryCustomImpl implements DDRBreakDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DDRBreakDetailResponseDTO> fetchWeekDaysBreakDetails(Long ddrWeekDaysDetailId) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_BREAK_DETAIL)
                .setParameter(DDR_WEEK_DAYS_ID, ddrWeekDaysDetailId);

        List<DDRBreakDetailResponseDTO> breakDetails =
                transformQueryToResultList(query, DDRBreakDetailResponseDTO.class);

        if (breakDetails.isEmpty())
            NO_BREAK_DETAIL_FOUND.get();

        return breakDetails;
    }

    @Override
    public List<DDRBreakResponseDTO> fetchWeekDaysBreakDetailForUpdateModal(Long ddrWeekDaysDetailId) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_BREAK_DETAIL_FOR_UPDATE_MODAL)
                .setParameter(DDR_WEEK_DAYS_ID, ddrWeekDaysDetailId);

        return transformQueryToResultList(query, DDRBreakResponseDTO.class);
    }

    private Supplier<NoContentFoundException> NO_BREAK_DETAIL_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DDR_BREAK_DETAIL);
        throw new NoContentFoundException(DDRBreakDetail.class);
    };
}
