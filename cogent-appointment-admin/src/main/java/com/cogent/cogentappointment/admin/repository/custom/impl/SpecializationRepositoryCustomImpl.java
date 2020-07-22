package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.SpecializationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Specialization;
import lombok.extern.slf4j.Slf4j;
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

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.admin.query.SpecializationQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class SpecializationRepositoryCustomImpl implements SpecializationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(String name, String code, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_SPECIALIZATION)
                .setParameter(NAME, name)
                .setParameter(CODE, code)
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(Long id, String name, String code, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_SPECIALIZATION_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(CODE, code)
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_SPECIALIZATION(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<SpecializationMinimalResponseDTO> results = transformQueryToResultList(
                query, SpecializationMinimalResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public SpecializationResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_DETAILS)
                .setParameter(ID, id);
        try {
            return transformQueryToSingleResult(query, SpecializationResponseDTO.class);
        } catch (NoResultException e) {
            throw SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationByDoctorId(Long DoctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_BY_DOCTOR_ID)
                .setParameter(ID, DoctorId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_BY_DOCTOR_ID)
                .setParameter(ID, doctorId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_SPECIALIZATION_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_SPECIALIZATION_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw SPECIALIZATION_NOT_FOUND.get();
        } else return results;
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,SPECIALIZATION,id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> SPECIALIZATION_NOT_FOUND = ()
            -> new NoContentFoundException(Specialization.class);


    private void error() {
        log.error(CONTENT_NOT_FOUND, SPECIALIZATION);
    }
}

