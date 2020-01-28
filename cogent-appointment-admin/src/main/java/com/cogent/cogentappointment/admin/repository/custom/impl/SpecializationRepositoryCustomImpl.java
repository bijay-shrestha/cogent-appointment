package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.Specialization;
import com.cogent.cogentappointment.admin.query.SpecializationQuery;
import com.cogent.cogentappointment.admin.repository.custom.SpecializationRepositoryCustom;
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
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Transactional(readOnly = true)
public class SpecializationRepositoryCustomImpl implements SpecializationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchSpecializationByName(String name) {
        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_FIND_SPECIALIZATION_COUNT_BY_NAME)
                .setParameter(QueryConstants.NAME, name);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchSpecializationByIdAndName(Long id, String name) {
        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_FIND_SPECIALIZATION_COUNT_BY_ID_AND_NAME)
                .setParameter(QueryConstants.ID, id)
                .setParameter(QueryConstants.NAME, name);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {

        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_SEARCH_SPECIALIZATION(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<SpecializationMinimalResponseDTO> results = QueryUtils.transformQueryToResultList(
                query, SpecializationMinimalResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationForDropDown() {
        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else return results;
    }

    @Override
    public SpecializationResponseDTO fetchDetailsById(Long id) {
        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_FETCH_SPECIALIZATION_DETAILS)
                .setParameter(QueryConstants.ID, id);
        try {
            return QueryUtils.transformQueryToSingleResult(query, SpecializationResponseDTO.class);
        } catch (NoResultException e) {
            throw SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long DoctorId) {
        Query query = QueryUtils.createQuery.apply(entityManager, SpecializationQuery.QUERY_TO_FETCH_SPECIALIZATION_BY_DOCTOR_ID)
                .setParameter(QueryConstants.ID, DoctorId);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else return results;
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> SPECIALIZATION_NOT_FOUND = ()
            -> new NoContentFoundException(Specialization.class);
}

