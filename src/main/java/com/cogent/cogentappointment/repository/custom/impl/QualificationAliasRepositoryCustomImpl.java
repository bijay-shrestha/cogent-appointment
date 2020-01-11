package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.QualificationAlias;
import com.cogent.cogentappointment.repository.custom.QualificationAliasRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.query.QualificationAliasQuery.QUERY_TO_FETCH_ACTIVE_QUALIFICATION_ALIAS;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class QualificationAliasRepositoryCustomImpl implements QualificationAliasRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveQualificationAlias() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_QUALIFICATION_ALIAS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw new NoContentFoundException(QualificationAlias.class);
        else return results;
    }
}
