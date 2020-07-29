package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.SalutationQuery;
import com.cogent.cogentappointment.client.repository.custom.SalutationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Salutation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.SalutationLog.SALUTATION;
import static com.cogent.cogentappointment.client.query.SalutationQuery.QUERY_TO_FETCH_ACTIVE_SALUTATION_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

@Repository
@Transactional(readOnly = true)
@Slf4j
public class SalutationRepositoryCustomImpl implements SalutationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveMinSalutation() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_SALUTATION_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SALUTATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<Salutation> validateSalutationCount(String ids) {
        Query query = createQuery.apply(entityManager, SalutationQuery.QUERY_TO_VALIDATE_SALUTATION_COUNT(ids));

        return query.getResultList();
    }

    private Supplier<NoContentFoundException> SALUTATION_NOT_FOUND = () ->
            new NoContentFoundException(Salutation.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, SALUTATION);
    }
}
