package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.AdminQuery;
import com.cogent.cogentappointment.admin.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.COMPANY_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN_META_INFO;
import static com.cogent.cogentappointment.admin.query.AdminQuery.QUERY_TO_FETCH_ADMIN_META_INFO;
import static com.cogent.cogentappointment.admin.query.CompanyAdminQuery.QUERY_TO_FETCH_COMPANY_ADMIN_META_INFO;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminMetaInfoRepositoryCustomImpl implements AdminMetaInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDTOS() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> list = transformQueryToResultList(query, AdminMetaInfoResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw new NoContentFoundException((AdminMetaInfo.class));
        }

        return list;
    }

    @Override
    public List<CompanyAdminMetaInfoResponseDTO> fetchCompanyAdminMetaInfoResponseDTOS() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_ADMIN_META_INFO);

        List<CompanyAdminMetaInfoResponseDTO> list = transformQueryToResultList(query,
                CompanyAdminMetaInfoResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw new NoContentFoundException((AdminMetaInfo.class));
        }

        return list;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByCompanyIdResponseDTOS(Long companyId) {
        Query query = createQuery.apply(entityManager, AdminQuery.QUERY_TO_FETCH_COMPANY_ADMIN_META_INFO_BY_COMPANY_ID)
                .setParameter(COMPANY_ID, companyId);

        List<AdminMetaInfoResponseDTO> list = transformQueryToResultList(query, AdminMetaInfoResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw new NoContentFoundException((AdminMetaInfo.class));
        }

        return list;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByClientIdResponseDTOS(Long id) {
        Query query = createQuery.apply(entityManager, AdminQuery.QUERY_TO_FETCH_COMPANY_ADMIN_META_INFO_BY_CLIENT_ID)
                .setParameter(HOSPITAL_ID, id);

        List<AdminMetaInfoResponseDTO> list = transformQueryToResultList(query, AdminMetaInfoResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw new NoContentFoundException((AdminMetaInfo.class));
        }

        return list;
    }

    private void error() {
        log.error(CONTENT_NOT_FOUND, ADMIN_META_INFO);
    }
}
