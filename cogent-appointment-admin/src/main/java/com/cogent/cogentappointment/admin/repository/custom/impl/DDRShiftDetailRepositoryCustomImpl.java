package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRShiftResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.DDRShiftDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_ID;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRShiftDetailQuery.QUERY_TO_FETCH_EXISTING_SHIFT_DETAIL;
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
    public List<DDRShiftResponseDTO> fetchExistingShift(Long ddrId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_SHIFT_DETAIL)
                .setParameter(DDR_ID, ddrId);

        return transformQueryToResultList(query, DDRShiftResponseDTO.class);
    }
}
