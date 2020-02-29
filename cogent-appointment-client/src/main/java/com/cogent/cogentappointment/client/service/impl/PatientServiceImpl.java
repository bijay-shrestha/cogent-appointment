package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.*;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientRelationInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientRepository;
import com.cogent.cogentappointment.client.service.PatientService;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.DELETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.PatientRelationInfoUtils.parseToPatientRelationInfo;
import static com.cogent.cogentappointment.client.utils.PatientUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              PatientMetaInfoRepository patientMetaInfoRepository,
                              HospitalPatientInfoRepository hospitalPatientInfoRepository,
                              PatientRelationInfoRepository patientRelationInfoRepository) {
        this.patientRepository = patientRepository;
        this.patientMetaInfoRepository = patientMetaInfoRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.patientRelationInfoRepository = patientRelationInfoRepository;
    }

    @Override
    public Patient saveSelfPatient(PatientRequestByDTO requestDTO, Hospital hospital) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Patient patient = fetchPatient(requestDTO);

        if (Objects.isNull(patient))
            patient = savePatientForSelf(requestDTO);

        Long hospitalPatientInfoCount = fetchHospitalPatientInfoCount(patient.getId(), hospital.getId());

        if (hospitalPatientInfoCount.intValue() <= 0) {
            HospitalPatientInfo hospitalPatientInfo = saveHospitalPatientInfo(
                    hospital, patient, requestDTO
            );

            savePatientMetaInfo(parseToPatientMetaInfo(patient, hospitalPatientInfo.getStatus()));
        }

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    /*CASE 1: BOOK APPOINTMENT FOR SELF AND THEN FOR OTHERS
   * CASE 2 : BOOK APPOINTMENT FOR OTHERS AND THEN FOR SELF
   *
   * FIRST SAVE REQUESTED BY PATIENT INFO IF IT HAS NOT BEEN SAVED (CASE 2)
   * */
    @Override
    public Patient saveOtherPatient(PatientRequestByDTO requestByPatientInfo,
                                    PatientRequestForDTO requestForPatientInfo,
                                    Hospital hospital) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Patient parentPatient = fetchPatient(requestByPatientInfo);

        if (Objects.isNull(parentPatient))
            parentPatient = savePatientForSelf(requestByPatientInfo);

        Patient childPatient = fetchPatient(requestForPatientInfo);

        if (!Objects.isNull(childPatient))
            PATIENT_DUPLICATION_EXCEPTION(
                    requestForPatientInfo.getName(),
                    requestForPatientInfo.getMobileNumber(),
                    requestForPatientInfo.getDateOfBirth()
            );

        childPatient = savePatientForOthers(requestForPatientInfo);

        Long hospitalPatientInfoCount = fetchHospitalPatientInfoCount(childPatient.getId(), hospital.getId());

        if (hospitalPatientInfoCount.intValue() <= 0) {

            HospitalPatientInfo hospitalPatientInfo = saveHospitalPatientInfo(
                    hospital, childPatient, requestForPatientInfo
            );

            savePatientMetaInfo(parseToPatientMetaInfo(childPatient, hospitalPatientInfo.getStatus()));

            PatientRelationInfo patientRelationInfo = fetchPatientRelationInfo(
                    parentPatient.getId(), childPatient.getId()
            );

            if (Objects.isNull(patientRelationInfo)) {
                savePatientRelationInfo(parentPatient, childPatient);
            } else {
                if (patientRelationInfo.getStatus().equals(DELETED))
                    patientRelationInfo.setStatus(ACTIVE);
            }
        }

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return childPatient;
    }

    @Override
    public Patient fetchPatientById(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        Patient patient = fetchPatient(id);


        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO responseDTO = patientRepository.searchForSelf(searchRequestDTO);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientMinSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<PatientMinimalResponseDTO> responseDTOS = patientRepository.searchForOthers
                (searchRequestDTO, pageable);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public PatientResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientResponseDTO responseDTOs = patientRepository.fetchPatientDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOs;
    }

    @Override
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        List<PatientSearchResponseDTO> patients =
                patientRepository.search(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patients;
    }

    @Override
    public void update(PatientUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PATIENT);

        Long hospitalId = getLoggedInHospitalId();

        Patient patient = fetchPatientByIdAndHospitalId(updateRequestDTO.getId(), hospitalId);

        Long patientCount = patientRepository.validatePatientDuplicity(updateRequestDTO, hospitalId);

        validatePatientDuplicity(patientCount, updateRequestDTO.getName(),
                updateRequestDTO.getMobileNumber(), updateRequestDTO.getDateOfBirth());

        HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository
                .fetchHospitalPatientInfoByPatientId(updateRequestDTO.getId());

        updatePatient(updateRequestDTO, patient);

        updateHospitalPatientInfo(updateRequestDTO, hospitalPatientInfo);

        PatientMetaInfo patientMetaInfo = patientMetaInfoRepository.fetchByPatientId(updateRequestDTO.getId());

        updatePatientMetaInfo(hospitalPatientInfo, patientMetaInfo, updateRequestDTO);

        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchMinPatientMetaInfo() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS =
                patientMetaInfoRepository.fetchMinPatientMetaInfo(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS =
                patientMetaInfoRepository.fetchActiveMinPatientMetaInfo(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void registerPatient(Long patientId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REGISTERING_PATIENT_PROCESS_STARTED);

        HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NoContentFoundException(Patient.class, "patientId", patientId.toString()));

        String latestRegistrationNumber =
                patientRepository.fetchLatestRegistrationNumber(hospitalPatientInfo.getHospital().getId());

        registerPatientDetails(hospitalPatientInfo, latestRegistrationNumber);

        log.info(REGISTERING_PATIENT_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    private Patient savePatientForSelf(PatientRequestByDTO requestDTO) {
        return savePatientInfo(
                requestDTO.getName(),
                requestDTO.getMobileNumber(),
                requestDTO.getDateOfBirth(),
                requestDTO.getESewaId(),
                requestDTO.getGender()
        );
    }

    private Patient savePatientForOthers(PatientRequestForDTO requestDTO) {
        return savePatientInfo(
                requestDTO.getName(),
                requestDTO.getMobileNumber(),
                requestDTO.getDateOfBirth(),
                null,
                requestDTO.getGender()
        );
    }

    private Patient savePatientInfo(String name, String mobileNumber, Date dateOfBirth,
                                    String eSewaId, Character genderCode) {

        Patient patient = parseToPatient(name, mobileNumber, dateOfBirth, eSewaId, fetchGender(genderCode));
        return patientRepository.save(patient);
    }

    private void validatePatientDuplicity(Long patientCount, String name, String mobileNumber,
                                          Date dateOfBirth) {

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

    private Patient fetchPatient(PatientRequestForDTO patientRequestForDTO) {
        return patientRepository.fetchPatient(
                patientRequestForDTO.getName(),
                patientRequestForDTO.getMobileNumber(),
                patientRequestForDTO.getDateOfBirth()
        );
    }

    private Long fetchHospitalPatientInfoCount(Long patientId,
                                               Long hospitalId) {

        return hospitalPatientInfoRepository.fetchHospitalPatientInfoCount(
                patientId,
                hospitalId
        );
    }

    private HospitalPatientInfo saveHospitalPatientInfo(Hospital hospital,
                                                        Patient patient,
                                                        PatientRequestByDTO requestByDTO) {

        HospitalPatientInfo hospitalPatientInfo = parseHospitalPatientInfo(
                hospital,
                patient,
                requestByDTO.getIsSelf(),
                requestByDTO.getEmail(),
                requestByDTO.getAddress()
        );

        return hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }

    private HospitalPatientInfo saveHospitalPatientInfo(Hospital hospital,
                                                        Patient patient,
                                                        PatientRequestForDTO requestForDTO) {

        HospitalPatientInfo hospitalPatientInfo = parseHospitalPatientInfo(
                hospital,
                patient,
                requestForDTO.getIsSelf(),
                requestForDTO.getEmail(),
                requestForDTO.getAddress()
        );

        return hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }

    private PatientRelationInfo fetchPatientRelationInfo(Long parentPatientId,
                                                         Long childPatientId) {
        return patientRelationInfoRepository.fetchPatientRelationInfo(
                parentPatientId, childPatientId);
    }

    private void savePatientRelationInfo(Patient parentPatientId,
                                         Patient childPatientId) {
        PatientRelationInfo patientRelationInfo = parseToPatientRelationInfo(parentPatientId, childPatientId);
        patientRelationInfoRepository.save(patientRelationInfo);
    }

    private Function<Long, NoContentFoundException> PATIENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Patient.class, "id", id.toString());
    };

    private Patient fetchPatientByIdAndHospitalId(Long id, Long hospitalId) {
        return patientRepository.fetchPatientByIdAndHospitalId(id, hospitalId).orElseThrow(() ->
                new NoContentFoundException(Patient.class));
    }

    private void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }

    private static void PATIENT_DUPLICATION_EXCEPTION(String name, String mobileNumber, Date dateOfBirth) {
        throw new DataDuplicationException(String.format(DUPLICATE_PATIENT_MESSAGE,
                name, mobileNumber, utilDateToSqlDate(dateOfBirth))
        );
    }

    private Patient fetchPatient(Long id) {
        return patientRepository.fetchPatientById(id)
                .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

}

