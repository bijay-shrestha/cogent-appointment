package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.SpecializationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Specialization;
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

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.SpecializationQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Transactional(readOnly = true)
public class SpecializationRepositoryCustomImpl implements SpecializationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_SPECIALIZATION)
                .setParameter(NAME, name)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicity(Long id, String name, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_SPECIALIZATION_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                         Long hospitalId,
                                                         Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_SPECIALIZATION(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<SpecializationMinimalResponseDTO> results = transformQueryToResultList(
                query, SpecializationMinimalResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinSpecialization(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else return results;
    }

    @Override
    public SpecializationResponseDTO fetchDetailsById(Long id, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_DETAILS)
                .setParameter(ID, id)
                .setParameter(HOSPITAL_ID, hospitalId);
        try {
            return transformQueryToSingleResult(query, SpecializationResponseDTO.class);
        } catch (NoResultException e) {
            throw SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_BY_DOCTOR_ID)
                .setParameter(ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw SPECIALIZATION_NOT_FOUND.get();
        else return results;
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> SPECIALIZATION_NOT_FOUND = ()
            -> new NoContentFoundException(Specialization.class);
}

