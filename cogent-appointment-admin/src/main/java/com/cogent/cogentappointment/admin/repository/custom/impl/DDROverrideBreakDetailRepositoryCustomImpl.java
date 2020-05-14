package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DDROverrideBreakDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDROverrideBreakDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_OVERRIDE_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_OVERRIDE_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDROverrideBreakDetailQuery.QUERY_TO_FETCH_OVERRIDE_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 14/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDROverrideBreakDetailRepositoryCustomImpl implements DDROverrideBreakDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DDRBreakDetailResponseDTO> fetchOverrideBreakDetail(Long ddrOverrideDetailId) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_OVERRIDE_BREAK_DETAIL)
                .setParameter(DDR_OVERRIDE_ID, ddrOverrideDetailId);

        List<DDRBreakDetailResponseDTO> breakDetails =
                transformQueryToResultList(query, DDRBreakDetailResponseDTO.class);

        if (breakDetails.isEmpty())
            NO_BREAK_DETAIL_FOUND.get();

        return breakDetails;
    }

    private Supplier<NoContentFoundException> NO_BREAK_DETAIL_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DDR_OVERRIDE_BREAK_DETAIL);
        throw new NoContentFoundException(DDROverrideBreakDetail.class);
    };
}
