package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AdminMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.client.repository.custom.PatientMetaInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.client.query.AdminQuery.QUERY_TO_FETCH_ADMIN_META_INFO;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Transactional(readOnly = true)
public class PatientMetaInfoRepositoryCustomImpl implements PatientMetaInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

}
