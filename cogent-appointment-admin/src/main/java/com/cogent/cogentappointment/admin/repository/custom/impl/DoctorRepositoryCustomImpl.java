package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
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
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ERROR_LOG;
import static com.cogent.cogentappointment.admin.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.admin.query.DoctorQuery.*;
import static com.cogent.cogentappointment.admin.utils.DoctorUtils.parseToDoctorUpdateResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
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
    public Long validateDoctorDuplicityForUpdate(Long id, String name,
                                                 String mobileNumber, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DOCTOR_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorMinimalResponseDTO> results = transformNativeQueryToResultList(
                query, DoctorMinimalResponseDTO.class);

        if (results.isEmpty()){

            error();
            throw DOCTOR_NOT_FOUND.get();
        }
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorForDropdown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()){
            error();
            throw DOCTOR_NOT_FOUND.get();
        }
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
    public List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_SPECIALIZATION_ID)
                .setParameter(ID, specializationId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()){
            error();
            throw DOCTOR_NOT_FOUND.get();
        }
        else return results;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()){
            error();
            throw DOCTOR_NOT_FOUND.get();
        }
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

    @Override
    public List<DoctorDropdownDTO> fetchDoctorAvatarInfo(Long hospitalId, Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVATAR_INFO(doctorId))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty()){
            error();
            throw DOCTOR_NOT_FOUND.get();
        }
        else return results;
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () ->
            new NoContentFoundException(Doctor.class);

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (doctorId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,DOCTOR,doctorId);
        throw new NoContentFoundException(Doctor.class, "doctorId", doctorId.toString());
    };

    public void error(){
        log.error(ERROR_LOG,DOCTOR);
    }
}
