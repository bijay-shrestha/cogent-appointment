package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorAppointmentCharge;
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

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.DOCTOR_APPOINTMENT_CHARGE;
import static com.cogent.cogentappointment.client.query.DoctorQuery.*;
import static com.cogent.cogentappointment.client.utils.DoctorUtils.parseToDoctorUpdateResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final MinIOProperties minIOProperties;

    public DoctorRepositoryCustomImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

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
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorMinimalResponseDTO> results = transformNativeQueryToResultList(
                query, DoctorMinimalResponseDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchActiveMinDoctor(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long doctorId, Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS)
                .setParameter(ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());
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
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DoctorDropdownDTO> fetchActiveDoctorByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_DOCTOR_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DoctorDropdownDTO> fetchMinDoctorByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> results = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (results.isEmpty())
            throw DOCTOR_NOT_FOUND.get();
        else return results;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long doctorId, Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DETAILS_FOR_UPDATE)
                .setParameter(ID, doctorId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId);

        return parseToDoctorUpdateResponseDTO(results.get(0));
    }

    @Override
    public Double fetchDoctorAppointmentFollowUpCharge(Long doctorId, Long hospitalId) {
        try {
            Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_FOLLOW_UP_CHARGE)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(HOSPITAL_ID, hospitalId);
            return (Double) query.getSingleResult();
        } catch (NoResultException ex) {
            throw DOCTOR_APPOINTMENT_CHARGE_NOT_FOUND.get();
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
            throw DOCTOR_APPOINTMENT_CHARGE_NOT_FOUND.get();
        }
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorAvatarInfo(Long hospitalId, Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVATAR_INFO(doctorId))
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        List<DoctorDropdownDTO> doctorAvatars = transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (doctorAvatars.isEmpty())
            throw DOCTOR_NOT_FOUND.get();

        else
            return doctorAvatars;
    }

    private Supplier<NoContentFoundException> DOCTOR_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR);
        throw new NoContentFoundException(Doctor.class);
    };

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (doctorId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, doctorId);
        throw new NoContentFoundException(Doctor.class, "doctorId", doctorId.toString());
    };

    private Supplier<NoContentFoundException> DOCTOR_APPOINTMENT_CHARGE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_APPOINTMENT_CHARGE);
        throw new NoContentFoundException(DoctorAppointmentCharge.class);
    };
}
