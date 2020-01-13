package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Doctor;
import com.cogent.cogentappointment.repository.custom.DoctorRepositoryCustom;
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

import static com.cogent.cogentappointment.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.query.DoctorQuery.*;
import static com.cogent.cogentappointment.utils.DoctorUtils.parseToDoctorUpdateResponseDTO;
import static com.cogent.cogentappointment.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorMinimalResponseDTO> results = transformNativeQueryToResultList(
                query, DoctorMinimalResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchDoctorForDropdown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long doctorId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS)
                .setParameter(ID, doctorId);
        try {
            return transformNativeQueryToSingleResult(query, DoctorDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId);
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_SPECIALIZATION_ID)
                .setParameter(ID, specializationId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long doctorId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS_FOR_UPDATE)
                .setParameter(ID, doctorId);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId);

        return parseToDoctorUpdateResponseDTO(results.get(0));
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (doctorId) -> {
        throw new NoContentFoundException(Doctor.class, "doctorId", doctorId.toString());
    };
}
