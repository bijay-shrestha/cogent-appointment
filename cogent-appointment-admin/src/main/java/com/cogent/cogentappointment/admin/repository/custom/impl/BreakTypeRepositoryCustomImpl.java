package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.BreakTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BreakType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.BreakTypeLog.BREAK_TYPE;
import static com.cogent.cogentappointment.admin.query.BreakTypeQuery.QUERY_TO_FETCH_BREAK_TYPE_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 06/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class BreakTypeRepositoryCustomImpl implements BreakTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchBreakTypeByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BREAK_TYPE_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> minInfo = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (minInfo.isEmpty())
            NO_BREAK_TYPE_FOUND.get();

        return minInfo;
    }

    private Supplier<NoContentFoundException> NO_BREAK_TYPE_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, BREAK_TYPE);
        throw new NoContentFoundException(BreakType.class);
    };
}
