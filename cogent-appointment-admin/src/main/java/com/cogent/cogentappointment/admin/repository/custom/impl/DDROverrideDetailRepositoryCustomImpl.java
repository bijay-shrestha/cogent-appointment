package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDROverrideDetailResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.DDROverrideDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_ID;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDROverrideDetailQuery.QUERY_TO_FETCH_DDR_OVERRIDE_DETAIL;
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
        Query query = entityManager.createQuery(QUERY_TO_FETCH_DDR_OVERRIDE_DETAIL)
                .setParameter(DDR_ID, ddrId);

        return transformQueryToResultList(query, DDROverrideDetailResponseDTO.class);
    }
}
