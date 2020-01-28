package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.repository.custom.QualificationRepositoryCustom;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.Qualification;
import com.cogent.cogentappointment.admin.query.QualificationQuery;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;


/**
 * @author smriti on 11/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class QualificationRepositoryCustomImpl implements QualificationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                        Pageable pageable) {

        Query query = QueryUtils.createQuery.apply(entityManager, QualificationQuery.QUERY_TO_SEARCH_QUALIFICATION.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<QualificationMinimalResponseDTO> results = QueryUtils.transformQueryToResultList(
                query, QualificationMinimalResponseDTO.class);

        if (results.isEmpty()) throw QUALIFICATION_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public QualificationResponseDTO fetchDetailsById(Long id) {
        Query query = QueryUtils.createQuery.apply(entityManager, QualificationQuery.QUERY_TO_FETCH_QUALIFICATION_DETAILS)
                .setParameter(QueryConstants.ID, id);
        try {
            return QueryUtils.transformQueryToSingleResult(query, QualificationResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Qualification.class, "id", id.toString());
        }
    }

    @Override
    public List<QualificationDropdownDTO> fetchActiveQualificationForDropDown() {
        Query query = QueryUtils.createQuery.apply(entityManager, QualificationQuery.QUERY_TO_FETCH_ACTIVE_QUALIFICATION_FOR_DROPDOWN);

        List<QualificationDropdownDTO> results = QueryUtils.transformQueryToResultList(query, QualificationDropdownDTO.class);

        if (results.isEmpty()) throw QUALIFICATION_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> QUALIFICATION_NOT_FOUND = () ->
            new NoContentFoundException(Qualification.class);

}
