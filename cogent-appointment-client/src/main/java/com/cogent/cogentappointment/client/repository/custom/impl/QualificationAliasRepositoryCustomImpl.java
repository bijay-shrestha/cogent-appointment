package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.qualificationAlias.QualificationAliasSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.qualificationAlias.QualificationAliasMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.QualificationAliasRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Qualification;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.NAME;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.QualificationAliasLog.QUALIFICATION_ALIAS;
import static com.cogent.cogentappointment.client.query.QualificationAliasQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class QualificationAliasRepositoryCustomImpl implements QualificationAliasRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveQualificationAlias() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_QUALIFICATION_ALIAS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()){
            error();
            throw QUALIFICATION_ALIAS_NOT_FOUND.get();
        }
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchQualificationAlias() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_QUALIFICATION_ALIAS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()){
            error();
            throw QUALIFICATION_ALIAS_NOT_FOUND.get();
        }
        else return results;
    }


    @Override
    public Long validateDuplicity(String name) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicity(Long id, String name) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(NAME, name)
                .setParameter(ID, id);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<QualificationAliasMinimalResponseDTO> search(QualificationAliasSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_QUALIFICATION_ALIAS.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<QualificationAliasMinimalResponseDTO> results = transformQueryToResultList(
                query, QualificationAliasMinimalResponseDTO.class);

        if (results.isEmpty()){
            error();
            throw QUALIFICATION_ALIAS_NOT_FOUND.get();
        }
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    private Supplier<NoContentFoundException> QUALIFICATION_ALIAS_NOT_FOUND = () ->
            new NoContentFoundException(QualificationAlias.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, QUALIFICATION_ALIAS);
    }

}
