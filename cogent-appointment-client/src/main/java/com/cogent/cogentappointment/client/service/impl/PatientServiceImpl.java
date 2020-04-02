package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.*;
import com.cogent.cogentappointment.client.dto.response.patient.*;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientRelationInfoRepository;
import com.cogent.cogentappointment.client.repository.PatientRepository;
import com.cogent.cogentappointment.client.service.PatientService;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.DELETED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.PatientMetaInfoUtils.updatePatientMetaInfoDetails;
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
    public PatientResponseDTOForOthersWithStatus searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                                 Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        List<PatientRelationInfoResponseDTO> patientRelationInfo =
                patientRepository.fetchPatientRelationInfo(searchRequestDTO);

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
    public void updateOtherPatientDetails(PatientUpdateDTOForOthers requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PATIENT);

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfoById(requestDTO.getHospitalPatientInfoId());

        validatePatientDuplicity(hospitalPatientInfo.getPatient().getId(),
                requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth()
        );

        updateOtherPatient(requestDTO, hospitalPatientInfo);

        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void deleteOtherPatient(PatientDeleteRequestDTOForOthers requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, PATIENT);

        PatientRelationInfo patientRelationInfo = patientRelationInfoRepository.fetchPatientRelationInfo(
                requestDTO.getParentPatientId(),
                requestDTO.getChildPatientId());

        if (Objects.isNull(patientRelationInfo))
            throw PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(requestDTO.getParentPatientId());

        patientRelationInfo.setStatus(DELETED);

        log.info(DELETING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
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
    public List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {
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

        Patient patientToUpdate = patientRepository.getPatientByHospitalPatientInfoId(updateRequestDTO.getId(),
                hospitalId);

        HospitalPatientInfo hospitalPatientInfoToBeUpdated = hospitalPatientInfoRepository
                .fetchHospitalPatientInfoByPatientId(updateRequestDTO.getId());

        validatePatientDuplicity(patientToUpdate.getId(),
                updateRequestDTO.getName(),
                updateRequestDTO.getMobileNumber(),
                updateRequestDTO.getDateOfBirth()
        );

        updatePatient(updateRequestDTO, patientToUpdate);

        saveHospitalPatientInfo(updateHospitalPatientInfo(updateRequestDTO, hospitalPatientInfoToBeUpdated));

        savePatientMetaInfo(updatePatientMetaInfo(hospitalPatientInfoToBeUpdated,
                patientMetaInfoRepository.fetchByPatientId(patientToUpdate.getId()),
                updateRequestDTO));

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
    public void registerPatient(Long patientId, Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REGISTERING_PATIENT_PROCESS_STARTED);

        HospitalPatientInfo hospitalPatientInfo =
                hospitalPatientInfoRepository.findByPatientAndHospitalId(patientId, hospitalId)
                        .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(patientId));

        if (hospitalPatientInfo.getIsRegistered().equals(NO)) {
            String latestRegistrationNumber =
                    patientRepository.fetchLatestRegistrationNumber(hospitalPatientInfo.getHospital().getId());

            registerPatientDetails(hospitalPatientInfo, latestRegistrationNumber);

            PatientMetaInfo patientMetaInfo = patientMetaInfoRepository.fetchByPatientId(patientId);
            updatePatientMetaInfoDetails(patientMetaInfo, hospitalPatientInfo.getRegistrationNumber());
        }

        log.info(REGISTERING_PATIENT_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Patient fetchPatient(PatientRequestForDTO patientRequestForDTO) {
        return patientRepository.fetchPatient(
                patientRequestForDTO.getName(),
                patientRequestForDTO.getMobileNumber(),
                patientRequestForDTO.getDateOfBirth()
        );
    }

    @Override
    public PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientMinDetailResponseDTO patientInfo = patientRepository.fetchDetailByAppointmentId(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patientInfo;
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
                .orElseThrow(() -> NoContentFoundException());
    }

    private void saveHospitalPatientInfo(HospitalPatientInfo hospitalPatientInfo) {
        hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }

    private void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }

    private NoContentFoundException NoContentFoundException() {
        log.error(CONTENT_NOT_FOUND, PATIENT);
        throw new NoContentFoundException(Patient.class);
    }


}

