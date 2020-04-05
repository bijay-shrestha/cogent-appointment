package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.ADMIN_META_INFO;
import static com.cogent.cogentappointment.client.query.AdminQuery.QUERY_TO_FETCH_ADMIN_META_INFO;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

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
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfo(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_META_INFO)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<AdminMetaInfoResponseDTO> list = transformQueryToResultList(query, AdminMetaInfoResponseDTO.class);

        if (list.isEmpty()){
            log.error(CONTENT_NOT_FOUND,ADMIN_META_INFO);
            throw new NoContentFoundException((AdminMetaInfo.class));
        }

        return list;
    }
}
