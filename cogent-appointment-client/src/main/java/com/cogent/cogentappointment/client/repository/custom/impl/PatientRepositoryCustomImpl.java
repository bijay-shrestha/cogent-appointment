package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.*;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.HOSPITAL_PATIENT_INFO_ID;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.client.query.PatientQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validatePatientDuplicity(PatientUpdateRequestDTO updateRequestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_UPDATED_PATIENT_DUPLICITY)
                .setParameter(NAME, updateRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(updateRequestDTO.getDateOfBirth()))
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_FOR_SELF)
                .setParameter(NAME, searchRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, searchRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchRequestDTO.getDateOfBirth()));

        try {
            return transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "name", searchRequestDTO.getName());
        }
    }

    @Override
    public List<Long> fetchChildPatientIds(PatientMinSearchRequestDTO searchRequestDTO) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_CHILD_PATIENT_IDS)
                .setParameter(NAME, searchRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, searchRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchRequestDTO.getDateOfBirth()));

        List<Long> childPatientIds = query.getResultList();

        if (childPatientIds.isEmpty()) throw new NoContentFoundException(Patient.class);

        return childPatientIds;
    }

    @Override
    public List<PatientMinimalResponseDTO> fetchMinPatientInfoForOthers(List<Long> childPatientIds,
                                                                        Pageable pageable) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_MIN_PATIENT_INFO_FOR_OTHERS(childPatientIds));

        List<PatientMinimalResponseDTO> patientMinInfo =
                transformQueryToResultList(query, PatientMinimalResponseDTO.class);

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (patientMinInfo.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            patientMinInfo.get(0).setTotalItems(totalItems);
            return patientMinInfo;
        }
    }

    @Override
    public PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientInfoId) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_MIN_PATIENT_DETAILS_FOR_OTHERS)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId);

        try {
            return transformQueryToSingleResult(query, PatientDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "id", hospitalPatientInfoId.toString());
        }
    }

    @Override
    public PatientResponseDTO fetchPatientDetailsById(Long id, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(ID, id)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, PatientResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "id", id.toString());
        }
    }

    @Override
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable,
                                                 Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<PatientSearchResponseDTO> patients = transformQueryToResultList(query, PatientSearchResponseDTO.class);

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (patients.isEmpty()) throw new NoContentFoundException(Patient.class);

        else {
            patients.get(0).setTotalItems(totalItems);
            return patients;
        }
    }

    @Override
    public Long countOverallRegisteredPatients(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public String fetchLatestRegistrationNumber(Long hospitalId) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER)
                .setParameter(HOSPITAL_ID, hospitalId);

        List results = query.getResultList();

        return results.isEmpty() ? null : results.get(0).toString();
    }

    @Override
    public Patient fetchPatient(String name, String mobileNumber, Date dateOfBirth) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_PATIENT, Patient.class)
                    .setParameter(NAME, name)
                    .setParameter(MOBILE_NUMBER, mobileNumber)
                    .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(dateOfBirth))
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAIL_BY_APPOINTMENT_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, PatientMinDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Patient.class, "appointmentId", appointmentId.toString());
        }
    }

}
