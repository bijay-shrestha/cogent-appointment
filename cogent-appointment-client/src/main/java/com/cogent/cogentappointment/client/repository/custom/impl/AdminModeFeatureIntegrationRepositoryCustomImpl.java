package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeApiIntegrationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeIntegrationSearchDTO;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeIntegrationSearchResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.IntegrationAdminModeQuery;
import com.cogent.cogentappointment.client.repository.custom.AdminModeFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.client.log.constants.IntegrationLog.CLIENT_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.client.query.IntegrationAdminModeQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
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
    public AdminModeIntegrationSearchDTO search(AdminModeApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_API_INTEGRATION_SEARCH_QUERY.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminModeIntegrationSearchResponseDTO> apiIntegrationSearchResponseDTOList =
                transformQueryToResultList(query, AdminModeIntegrationSearchResponseDTO.class);

        AdminModeIntegrationSearchDTO adminModeIntegrationSearchDTO = new AdminModeIntegrationSearchDTO();
        if (apiIntegrationSearchResponseDTOList.isEmpty())
            throw ADMIN_MODE_API_INTEGRATIONS_NOT_FOUND.get();

        else {
            adminModeIntegrationSearchDTO.setSearchResponseDTOS(apiIntegrationSearchResponseDTOList);
            adminModeIntegrationSearchDTO.setTotalItems(totalItems);
            return adminModeIntegrationSearchDTO;
        }
    }

    @Override
    public List<AdminFeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO() {
        Query query = createQuery.apply(entityManager, APPOINTMENT_MODE_FEATURES_INTEGRATION_API_QUERY);

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

    @Override
    public Long findAppointmentModeWiseFeatureAndRequestMethod(Long appointmentModeId, Long featureTypeId, Long requestMethodId) {
        Query query = createQuery.apply(entityManager,
                IntegrationAdminModeQuery.VALIDATE_ADMIN_MODE_REQUEST_METHOD_AND_FEATURE)
                .setParameter(API_FEATURE_ID, featureTypeId)
                .setParameter(APPOINTMENT_MODE_ID, appointmentModeId)
                .setParameter(API_REQUEST_METHOD_ID, requestMethodId);


        return (Long) query.getSingleResult();
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
