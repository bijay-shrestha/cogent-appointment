package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.*;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.HOSPITAL_NULL_MESSAGE;
import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.HOSPITAL_PATIENT_INFO_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.PATIENT_ID;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS;
import static com.cogent.cogentappointment.client.query.PatientQuery.*;
import static com.cogent.cogentappointment.client.utils.PatientUtils.parseHospitalWisePatientInfo;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class PatientRepositoryCustomImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validatePatientDuplicity(Long patientId,
                                         String name,
                                         String mobileNumber,
                                         Date dateOfBirth) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_PATIENT_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(dateOfBirth))
                .setParameter(ID, patientId);

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
            log.error(PATIENT_NOT_FOUND_BY_NAME, searchRequestDTO.getName());
            throw new NoContentFoundException(Patient.class, "name", searchRequestDTO.getName());
        }
    }

    /*FIRST FETCH GENERAL INFO OF PATIENT AND IT EXISTS
    * FETCH ITS INFO HOSPITAL WISE*/
    @Override
    public PatientDetailResponseDTO searchForSelfHospitalWise(PatientMinSearchRequestDTO searchRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_INFO_FOR_SELF)
                .setParameter(NAME, searchRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, searchRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchRequestDTO.getDateOfBirth()));

        try {
            PatientDetailResponseDTO patientInfo = transformQueryToSingleResult(query, PatientDetailResponseDTO.class);

            searchPatientHospitalWiseInfo(searchRequestDTO.getHospitalId(), patientInfo);

            return patientInfo;
        } catch (NoResultException e) {
            log.error(PATIENT_NOT_FOUND_BY_NAME, searchRequestDTO.getName());
            throw new NoContentFoundException(Patient.class, "name", searchRequestDTO.getName());
        }
    }

    @Override
    public List<PatientRelationInfoResponseDTO> fetchPatientRelationInfo(
            PatientMinSearchRequestDTO searchRequestDTO) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_CHILD_PATIENT_IDS)
                .setParameter(NAME, searchRequestDTO.getName())
                .setParameter(MOBILE_NUMBER, searchRequestDTO.getMobileNumber())
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(searchRequestDTO.getDateOfBirth()));

        List<PatientRelationInfoResponseDTO> results =
                transformQueryToResultList(query, PatientRelationInfoResponseDTO.class);

        if (results.isEmpty())
            PATIENT_NOT_FOUND.get();

        return results;
    }

    @Override
    public List<PatientRelationInfoResponseDTO> fetchPatientRelationInfoHospitalWise(String name,
                                                                                     String mobileNumber,
                                                                                     Date dateOfBirth,
                                                                                     Long hospitalId) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_CHILD_PATIENT_IDS(hospitalId))
                .setParameter(NAME, name)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(DATE_OF_BIRTH, utilDateToSqlDate(dateOfBirth));

        List<PatientRelationInfoResponseDTO> results =
                transformQueryToResultList(query, PatientRelationInfoResponseDTO.class);

        if (results.isEmpty())
            PATIENT_NOT_FOUND.get();

        return results;
    }

    @Override
    public PatientResponseDTOForOthers fetchMinPatientInfoForOthers(
            List<PatientRelationInfoResponseDTO> patientRelationInfo,
            Pageable pageable) {

        String childPatientIds = patientRelationInfo.stream()
                .map(info -> info.getChildPatientId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        return PatientResponseDTOForOthers.builder()
                .parentPatientId(patientRelationInfo.get(0).getParentPatientId())
                .patientInfo(fetchPatientInfo(pageable, childPatientIds))
                .build();
    }

    private List<PatientMinResponseDTOForOthers> fetchPatientInfo(Pageable pageable,
                                                                  String childPatientIds) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_MIN_PATIENT_INFO_FOR_OTHERS(childPatientIds));

        List<PatientMinResponseDTOForOthers> patientMinInfo =
                transformQueryToResultList(query, PatientMinResponseDTOForOthers.class);

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        if (patientMinInfo.isEmpty())
            throw PATIENT_NOT_FOUND.get();

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
            log.error(PATIENT_NOT_FOUND_BY_HOSPITAL_PATIENT_INFO_ID, PATIENT, hospitalPatientInfoId);
            throw new NoContentFoundException(Patient.class, "id", hospitalPatientInfoId.toString());
        }
    }

    @Override
    public PatientResponseDTO fetchPatientDetailsById(Long hospitalPatientInfoId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_DETAILS_BY_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, PatientResponseDTO.class);
        } catch (NoResultException e) {
            log.error(PATIENT_NOT_FOUND_BY_HOSPITAL_PATIENT_INFO_ID, PATIENT, hospitalPatientInfoId);
            throw new NoContentFoundException(Patient.class, "id", hospitalPatientInfoId.toString());
        }
    }

    @Override
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable,
                                                 Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PATIENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        Integer totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<PatientSearchResponseDTO> patients = transformQueryToResultList(query, PatientSearchResponseDTO.class);

        if (patients.isEmpty())
            PATIENT_NOT_FOUND.get();

        patients.get(0).setTotalItems(totalItems);

        return patients;
    }

    @Override
    public Long countOverallRegisteredPatients(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_OVERALL_REGISTERED_PATIENTS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public String fetchLatestRegistrationNumber(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_REGISTRATION_NUMBER)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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
            log.error(PATIENT_NOT_FOUND_BY_APPOINTMENT_ID, appointmentId);
            throw new NoContentFoundException(Patient.class, "appointmentId", appointmentId.toString());
        }
    }

    @Override
    public Patient getPatientByHospitalPatientInfoId(Long hospitalPatientInfoId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_BY_HOSPITAL_PATIENT_INFO_ID)
                .setParameter(HOSPITAL_PATIENT_INFO_ID, hospitalPatientInfoId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Patient) query.getSingleResult();
    }

    @Override
    public List<DropDownResponseDTO> fetchPatientEsewaId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ESEWA_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> list = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (list.isEmpty()) PATIENT_NOT_FOUND();

        return list;
    }

    private Supplier<NoContentFoundException> PATIENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    };

    private void searchPatientHospitalWiseInfo(Long hospitalId,
                                               PatientDetailResponseDTO patientInfo) {

        if (Objects.isNull(hospitalId))
            throw new BadRequestException(HOSPITAL_NULL_MESSAGE);

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PATIENT_HOSPITAL_WISE_INFO(hospitalId))
                .setParameter(PATIENT_ID, patientInfo.getPatientId());

        List<Object[]> result = query.getResultList();

        if (!ObjectUtils.isEmpty(result))
            parseHospitalWisePatientInfo(patientInfo, result.get(0));
    }

    private void PATIENT_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    }


}
