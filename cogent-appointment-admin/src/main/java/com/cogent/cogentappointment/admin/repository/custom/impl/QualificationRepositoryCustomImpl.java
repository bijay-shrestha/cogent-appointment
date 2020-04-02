package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.qualification.QualificationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.qualification.QualificationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.QualificationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Qualification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.QualificationQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 11/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class QualificationRepositoryCustomImpl implements QualificationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name, Long universityId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(UNIVERSITY_ID, universityId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicity(Long id, String name, Long universityId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(UNIVERSITY_ID, universityId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<QualificationMinimalResponseDTO> search(QualificationSearchRequestDTO searchRequestDTO,
                                                        Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_QUALIFICATION.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<QualificationMinimalResponseDTO> results = transformQueryToResultList(
                query, QualificationMinimalResponseDTO.class);

        if (results.isEmpty()) throw QUALIFICATION_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public QualificationResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_QUALIFICATION_DETAILS)
                .setParameter(ID, id);
        try {
            return transformQueryToSingleResult(query, QualificationResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Qualification.class, "id", id.toString());
        }
    }

    @Override
    public List<QualificationDropdownDTO> fetchActiveQualificationForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_QUALIFICATION_FOR_DROPDOWN);

        List<QualificationDropdownDTO> results = transformQueryToResultList(query, QualificationDropdownDTO.class);

        if (results.isEmpty()) throw QUALIFICATION_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinQualification() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MIN_QUALIFICATION);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw QUALIFICATION_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> QUALIFICATION_NOT_FOUND = () ->
            new NoContentFoundException(Qualification.class);

}
