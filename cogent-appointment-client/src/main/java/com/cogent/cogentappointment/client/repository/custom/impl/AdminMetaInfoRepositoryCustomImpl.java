package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.AdminMetaInfo;
import com.cogent.cogentappointment.client.query.AdminQuery;
import com.cogent.cogentappointment.client.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.client.utils.commons.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Transactional(readOnly = true)
public class AdminMetaInfoRepositoryCustomImpl implements AdminMetaInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDTOS() {
        Query query = QueryUtils.createQuery.apply(entityManager, AdminQuery.QUERY_TO_FETCH_ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> list = QueryUtils.transformQueryToResultList(query, AdminMetaInfoResponseDTO.class);

        if (list.isEmpty()) throw new NoContentFoundException((AdminMetaInfo.class));

        return list;
    }
}
