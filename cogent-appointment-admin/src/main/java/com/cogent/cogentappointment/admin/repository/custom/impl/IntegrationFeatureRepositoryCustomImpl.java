package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.IntegrationQuery;
import com.cogent.cogentappointment.admin.repository.custom.IntegrationFeatureRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.FEATURES;
import static com.cogent.cogentappointment.admin.query.IntegrationQuery.QUERY_TO_FETCH_MIN_FEATURES;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak on 2020-05-19
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class IntegrationFeatureRepositoryCustomImpl implements IntegrationFeatureRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveFeatureType() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MIN_FEATURES);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            featureError();
            throw FEATURE_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveApiIntegrationChannel() {
        Query query = createQuery.apply(entityManager, IntegrationQuery.QUERY_TO_FETCH_MIN_INTEGRATION_CHANNEL);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            featureError();
            throw FEATURE_NOT_FOUND.get();
        } else return results;
    }

    private Supplier<NoContentFoundException> FEATURE_NOT_FOUND = () ->
            new NoContentFoundException(Feature.class);

    private void featureError() {
        log.error(CONTENT_NOT_FOUND, FEATURES);
    }

}
