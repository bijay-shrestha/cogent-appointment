package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDROverrideBreakDetailResponseDTO;
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
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_OVERRIDE_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_OVERRIDE_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDROverrideBreakDetailQuery.QUERY_TO_FETCH_EXISTING_OVERRIDE_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDROverrideBreakDetailQuery.QUERY_TO_FETCH_OVERRIDE_BREAK_DETAIL;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
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
    public List<DDRBreakDetailResponseDTO> fetchExistingOverrideBreakDetail(Long ddrOverrideId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_OVERRIDE_BREAK_DETAIL)
                .setParameter(DDR_OVERRIDE_ID, ddrOverrideId);

        List<DDRBreakDetailResponseDTO> breakDetails =
                transformQueryToResultList(query, DDRBreakDetailResponseDTO.class);

        if (breakDetails.isEmpty())
            NO_BREAK_DETAIL_FOUND.apply(ddrOverrideId);

        return breakDetails;
    }

    @Override
    public List<DDROverrideBreakDetailResponseDTO> fetchOverrideBreakDetail(Long ddrOverrideId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_OVERRIDE_BREAK_DETAIL)
                .setParameter(DDR_OVERRIDE_ID, ddrOverrideId);

        List<DDROverrideBreakDetailResponseDTO> breakDetails =
                transformQueryToResultList(query, DDROverrideBreakDetailResponseDTO.class);

        if (breakDetails.isEmpty())
            NO_BREAK_DETAIL_FOUND.apply(ddrOverrideId);

        return breakDetails;
    }

    private Function<Long, NoContentFoundException> NO_BREAK_DETAIL_FOUND = (ddrOverrideId) -> {
        log.error(CONTENT_NOT_FOUND, DDR_OVERRIDE_BREAK_DETAIL);
        throw new NoContentFoundException(DDROverrideBreakDetail.class, "ddrOverrideId", ddrOverrideId.toString());
    };
}
