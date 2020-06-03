package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationSearchResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeFeatureIntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.ADMIN_MODE_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.ADMIN_MODE_API_INTEGRATION_SEARCH_QUERY;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak ON 2020/06/03-10:05 AM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminModeFeatureIntegrationRepositoryCustomImpl implements
        AdminModeFeatureIntegrationRepositoryCustom {

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
            throw ADMIN_MODE_API_INTEGRATION_NOT_FOUND.get();

        else {
            adminModeIntegrationSearchDTO.setSearchResponseDTOS(apiIntegrationSearchResponseDTOList);
            adminModeIntegrationSearchDTO.setTotalItems(totalItems);
            return adminModeIntegrationSearchDTO;
        }

    }

    private Supplier<NoContentFoundException> ADMIN_MODE_API_INTEGRATION_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, ADMIN_MODE_FEATURE_INTEGRATION);
        throw new NoContentFoundException
                (AdminModeFeatureIntegration.class);
    };
}
