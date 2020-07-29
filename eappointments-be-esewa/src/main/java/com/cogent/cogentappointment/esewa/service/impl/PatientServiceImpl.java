package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.patient.*;
import com.cogent.cogentappointment.esewa.dto.response.patient.*;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.esewa.repository.PatientRelationInfoRepository;
import com.cogent.cogentappointment.esewa.repository.PatientRepository;
import com.cogent.cogentappointment.esewa.service.PatientService;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.DELETED;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.esewa.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.esewa.utils.PatientUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    private final Validator validator;

    public PatientServiceImpl(PatientRepository patientRepository,
                              HospitalPatientInfoRepository hospitalPatientInfoRepository,
                              PatientRelationInfoRepository patientRelationInfoRepository,
                              Validator validator) {
        this.patientRepository = patientRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.patientRelationInfoRepository = patientRelationInfoRepository;
        this.validator = validator;
    }

    @Override
    public Patient saveSelfPatient(PatientRequestByDTO requestByPatientInfo) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Patient patient = fetchPatient(requestByPatientInfo);

        if (Objects.isNull(patient))
            patient = savePatientForSelf(requestByPatientInfo);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public Patient saveOtherPatient(PatientRequestForDTO requestForPatientInfo) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Patient patient = savePatientForOthers(requestForPatientInfo);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public Patient fetchPatientById(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        Patient patient = patientRepository.fetchPatientById(id)
                .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public PatientDetailResponseDTOWithStatus searchForSelf(PatientMinSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO responseDTO = patientRepository.searchForSelf(searchRequestDTO);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return parseToPatientDetailResponseDTOWithStatus(responseDTO);
    }

    @Override
    public PatientDetailResponseDTOWithStatus searchForSelfHospitalWise(@Valid PatientMinSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        validateConstraintViolation(validator.validate(searchRequestDTO));

        PatientDetailResponseDTO responseDTO = patientRepository.searchForSelfHospitalWise(searchRequestDTO);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return parseToPatientDetailResponseDTOWithStatus(responseDTO);
    }

    @Override
    public PatientResponseDTOForOthersWithStatus searchForOthers(@Valid PatientMinSearchRequestDTO searchRequestDTO,
                                                                 Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        validateConstraintViolation(validator.validate(searchRequestDTO));

        List<PatientRelationInfoResponseDTO> patientRelationInfo =
                patientRepository.fetchPatientRelationInfo(searchRequestDTO);

        PatientResponseDTOForOthers infoForOthers =
                patientRepository.fetchMinPatientInfoForOthers(patientRelationInfo, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return parseToPatientMinResponseDTOForOthersWithStatus(infoForOthers);
    }

    @Override
    public PatientResponseDTOForOthersWithStatus searchForOthersHospitalWise(@Valid PatientMinSearchRequestDTO searchRequestDTO,
                                                                             Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        validateConstraintViolation(validator.validate(searchRequestDTO));

        List<PatientRelationInfoResponseDTO> patientRelationInfo =
                patientRepository.fetchPatientRelationInfoHospitalWise(
                        searchRequestDTO.getName(),
                        searchRequestDTO.getMobileNumber(),
                        searchRequestDTO.getDateOfBirth(),
                        searchRequestDTO.getHospitalId()
                );

        PatientResponseDTOForOthers patientMinInfo =
                patientRepository.fetchMinPatientInfoForOthers(patientRelationInfo, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return parseToPatientMinResponseDTOForOthersWithStatus(patientMinInfo);
    }

    @Override
    public PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientInfoId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO patientInfo =
                patientRepository.fetchMinPatientDetailsOfOthers(hospitalPatientInfoId);

        log.info(FETCHING_PROCESS_STARTED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patientInfo;
    }

    @Override
    public void updateOtherPatientDetails(@Valid PatientUpdateDTOForOthers requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PATIENT);

        validateConstraintViolation(validator.validate(requestDTO));

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfoById(requestDTO.getHospitalPatientInfoId());

        validatePatientDuplicity(hospitalPatientInfo.getPatient().getId(),
                requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth()
        );

        updateOtherPatient(requestDTO, hospitalPatientInfo);

        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void deleteOtherPatient(@Valid PatientDeleteRequestDTOForOthers requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, PATIENT);

        validateConstraintViolation(validator.validate(requestDTO));

        PatientRelationInfo patientRelationInfo = patientRelationInfoRepository.fetchPatientRelationInfo(
                requestDTO.getParentPatientId(),
                requestDTO.getChildPatientId());

        if (Objects.isNull(patientRelationInfo))
            throw PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(requestDTO.getParentPatientId());

        patientRelationInfo.setStatus(DELETED);

        log.info(DELETING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Patient fetchPatient(PatientRequestForDTO patientRequestForDTO) {
        return patientRepository.fetchPatient(
                patientRequestForDTO.getName(),
                patientRequestForDTO.getMobileNumber(),
                patientRequestForDTO.getDateOfBirth()
        );
    }

    private Patient savePatientForSelf(PatientRequestByDTO requestDTO) {
        return savePatientInfo(
                requestDTO.getName(),
                requestDTO.getMobileNumber(),
                requestDTO.getDateOfBirth(),
                requestDTO.getESewaId(),
                requestDTO.getGender(),
                requestDTO.getIsAgent()
        );
    }

    private Patient savePatientForOthers(PatientRequestForDTO requestDTO) {
        return savePatientInfo(
                requestDTO.getName(),
                requestDTO.getMobileNumber(),
                requestDTO.getDateOfBirth(),
                null,
                requestDTO.getGender(),
                NO
        );
    }

    private Patient savePatientInfo(String name, String mobileNumber, Date dateOfBirth,
                                    String eSewaId, Character genderCode, Character isAgent) {

        Patient patient =
                parseToPatient(name, mobileNumber, dateOfBirth, eSewaId, fetchGender(genderCode), isAgent);
        return patientRepository.save(patient);
    }

    private void validatePatientDuplicity(Long patientId, String name, String mobileNumber,
                                          Date dateOfBirth) {

        Long patientCount = patientRepository.validatePatientDuplicity(patientId, name, mobileNumber, dateOfBirth);

        if (patientCount.intValue() > 0)
            PATIENT_DUPLICATION_EXCEPTION(name, mobileNumber, dateOfBirth);
    }

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Patient fetchPatient(PatientRequestByDTO patientRequestByDTO) {
        return patientRepository.fetchPatient(
                patientRequestByDTO.getName(),
                patientRequestByDTO.getMobileNumber(),
                patientRequestByDTO.getDateOfBirth()
        );
    }

    private Function<Long, NoContentFoundException> PATIENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, PATIENT, id);
        throw new NoContentFoundException(Patient.class, "patientId", id.toString());
    };

    private static void PATIENT_DUPLICATION_EXCEPTION(String name, String mobileNumber, Date dateOfBirth) {
        log.error(NAME_AND_MOBILE_NUMBER_DUPLICATION_ERROR, PATIENT, name, mobileNumber, utilDateToSqlDate(dateOfBirth));
        throw new DataDuplicationException(String.format(DUPLICATE_PATIENT_MESSAGE,
                name, mobileNumber, utilDateToSqlDate(dateOfBirth))
        );
    }

    private HospitalPatientInfo fetchHospitalPatientInfoById(Long hospitalPatientInfoId) {
        return hospitalPatientInfoRepository.fetchHospitalPatientInfoById(hospitalPatientInfoId)
                .orElseThrow(this::NoContentFoundException);
    }

    private NoContentFoundException NoContentFoundException() {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    }

}

