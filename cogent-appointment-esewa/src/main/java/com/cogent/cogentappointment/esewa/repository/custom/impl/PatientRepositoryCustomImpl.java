package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientMinResponseDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientRelationInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientResponseDTOForOthers;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.PatientRepositoryCustom;
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

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HOSPITAL_NULL_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.PatientQueryConstants.HOSPITAL_PATIENT_INFO_ID;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.esewa.query.PatientQuery.*;
import static com.cogent.cogentappointment.esewa.utils.PatientUtils.parseHospitalWisePatientInfo;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;


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

    /*FIRST FETCH GENERAL INFO OF PATIENT AND IF EXISTS FETCH ITS INFO HOSPITAL WISE*/
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
            PATIENT_NOT_FOUND.get();


        patientMinInfo.get(0).setTotalItems(totalItems);
        return patientMinInfo;
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

    private Supplier<NoContentFoundException> PATIENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    };

}
