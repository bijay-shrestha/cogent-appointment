package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRShiftMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRShiftDetailResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DDRShiftDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRShiftDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_ID;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_SHIFT_WISE;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRShiftDetailQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 08/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDRShiftDetailRepositoryCustomImpl implements DDRShiftDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DDRShiftMinResponseDTO> fetchMinShiftInfo(Long ddrId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MIN_SHIFT_INFO)
                .setParameter(DDR_ID, ddrId);

        List<DDRShiftMinResponseDTO> shiftDetail =
                transformQueryToResultList(query, DDRShiftMinResponseDTO.class);

        if(shiftDetail.isEmpty())
            NO_SHIFT_INFO_FOUND.get();

        return shiftDetail;
    }

    @Override
    public Long validateDDRShiftCount(List<Long> shiftIds, Long ddrId) {
        String shifts =  StringUtils.join(shiftIds, COMMA_SEPARATED);

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_SHIFT_COUNT(shifts, ddrId));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DDRShiftDetailResponseDTO> fetchShiftDetails(Long ddrId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SHIFT_DETAILS)
                .setParameter(DDR_ID, ddrId);

        List<DDRShiftDetailResponseDTO> shiftDetail =
                transformQueryToResultList(query, DDRShiftDetailResponseDTO.class);

        if(shiftDetail.isEmpty())
            NO_SHIFT_INFO_FOUND.get();

        return shiftDetail;
    }

    private Supplier<NoContentFoundException> NO_SHIFT_INFO_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DDR_SHIFT_WISE);
        throw new NoContentFoundException(DDRShiftDetail.class);
    };
}
