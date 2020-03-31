package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN_META_INFO;
import static com.cogent.cogentappointment.admin.query.AdminQuery.QUERY_TO_FETCH_ADMIN_META_INFO;
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

    private void error() {
        log.error(CONTENT_NOT_FOUND, ADMIN_META_INFO);
    }
}
