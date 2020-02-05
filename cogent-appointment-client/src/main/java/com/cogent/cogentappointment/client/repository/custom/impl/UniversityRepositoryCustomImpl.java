package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.UniversityRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.University;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.NAME;
import static com.cogent.cogentappointment.client.query.UniversityQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 08/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class UniversityRepositoryCustomImpl implements UniversityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicity(Long id, String name) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name);

        return (Long) query.getSingleResult();
    }


    @Override
    public List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_UNIVERSITY.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<UniversityMinimalResponseDTO> results = transformQueryToResultList(
                query, UniversityMinimalResponseDTO.class);

        if (results.isEmpty()) throw UNIVERSITY_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public UniversityResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_UNIVERSITY_DETAILS)
                .setParameter(ID, id);
        try {
            return transformQueryToSingleResult(query, UniversityResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(University.class, "id", id.toString());
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveUniversity() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_UNIVERSITY);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw UNIVERSITY_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> UNIVERSITY_NOT_FOUND = () ->
            new NoContentFoundException(University.class);

}
