package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.*;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.client.utils.commons.PageableUtils;
import com.cogent.cogentappointment.persistence.model.Doctor;
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
import static com.cogent.cogentappointment.client.query.DoctorQuery.*;
import static com.cogent.cogentappointment.client.utils.DoctorUtils.parseToDoctorUpdateResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDoctorDuplicity(String name, String mobileNumber, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DOCTOR_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDoctorDuplicityForUpdate(Long id, String name, String mobileNumber, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DOCTOR_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Long hospitalId,
                                                 Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<DoctorMinimalResponseDTO> results = transformNativeQueryToResultList(
                query, DoctorMinimalResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchActiveMinDoctor(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long doctorId, Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS)
                .setParameter(ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId);
        try {
            return transformNativeQueryToSingleResult(query, DoctorDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId);
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorBySpecializationAndHospitalId(Long specializationId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_SPECIALIZATION_ID)
                .setParameter(ID, specializationId)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long doctorId, Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS_FOR_UPDATE)
                .setParameter(ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId);

        return parseToDoctorUpdateResponseDTO(results.get(0));
    }

    @Override
    public List<DoctorMinResponseDTO> fetchDoctorMinInfo(Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_MIN_DOCTOR_INFO)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorMinResponseDTO> results = transformNativeQueryToResultList(query, DoctorMinResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();

        return results;
    }

    @Override
    public Double fetchDoctorAppointmentFollowUpCharge(Long doctorId, Long hospitalId) {
        try {
            Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(HOSPITAL_ID, hospitalId);
            return (Double) query.getSingleResult();
        } catch (NoResultException ex) {
            throw DOCTOR_NOT_FOUND.get();
        }
    }

    @Override
    public Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId) {
        try {
            Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_CHARGE)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(HOSPITAL_ID, hospitalId);

            return (Double) query.getSingleResult();
        } catch (NoResultException ex) {
            throw DOCTOR_NOT_FOUND.get();
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorAvatarInfo(Long hospitalId, Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVATAR_INFO(doctorId))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()) throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (doctorId) -> {
        throw new NoContentFoundException(Doctor.class, "doctorId", doctorId.toString());
    };
}
