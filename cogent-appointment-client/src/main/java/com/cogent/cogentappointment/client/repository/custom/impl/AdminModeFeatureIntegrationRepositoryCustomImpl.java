package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeApiIntegrationResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AdminModeFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.ADMIN_MODE_FEATURE_INTEGRATION_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.client.log.constants.IntegrationLog.CLIENT_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.client.query.IntegrationAdminModeQuery.ADMIN_MODE_INTEGRATION_DETAILS_API_QUERY;
import static com.cogent.cogentappointment.client.query.IntegrationAdminModeQuery.APPOINTMENT_MODE_FEATURES_INTEGRATION_API_QUERY;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author rupak ON 2020/06/03-10:05 AM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminModeFeatureIntegrationRepositoryCustomImpl implements AdminModeFeatureIntegrationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdminFeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO(Long hospitalId) {
        Query query = createQuery.apply(entityManager, APPOINTMENT_MODE_FEATURES_INTEGRATION_API_QUERY)
                .setParameter(HOSPITAL_ID,hospitalId);

        List<AdminFeatureIntegrationResponse> responseDTOList =
                transformQueryToResultList(query, AdminFeatureIntegrationResponse.class);

        return responseDTOList;
    }

    @Override
    public AdminModeApiIntegrationResponseDTO findAdminModeFeatureIntegration(Long id) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_INTEGRATION_DETAILS_API_QUERY)
                .setParameter(ADMIN_MODE_FEATURE_INTEGRATION_ID, id);


        try {
            return transformQueryToSingleResult(query, AdminModeApiIntegrationResponseDTO.class);
        } catch (NoResultException e) {
            throw ADMIN_MODE_API_INTEGRATION_NOT_FOUND.apply(id);
        }
    }

    private Supplier<NoContentFoundException> ADMIN_MODE_API_INTEGRATIONS_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, ADMIN_MODE_FEATURE_INTEGRATION);
        throw new NoContentFoundException(AdminModeFeatureIntegration.class);
    };

    private Function<Long, NoContentFoundException> ADMIN_MODE_API_INTEGRATION_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND, ADMIN_MODE_FEATURE_INTEGRATION);
        throw new NoContentFoundException(AdminModeApiFeatureIntegration.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> CLIENT_API_INTEGRATION_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException(ClientFeatureIntegration.class);
    };


}
