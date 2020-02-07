package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import com.cogent.cogentappointment.admin.utils.PatientUtils;
import com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final HospitalService hospitalService;

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              HospitalService hospitalService,
                              PatientMetaInfoRepository patientMetaInfoRepository) {
        this.patientRepository = patientRepository;
        this.hospitalService = hospitalService;
        this.patientMetaInfoRepository = patientMetaInfoRepository;
    }

    @Override
    public Patient save(PatientRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT);

        Long patientCount = patientRepository.fetchPatientForValidation(requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth());

        validatePatientDuplicity(patientCount, requestDTO.getName(),
                requestDTO.getMobileNumber(), requestDTO.getDateOfBirth());

        Patient patient = savePatient(requestDTO);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public Patient fetchPatient(Long id) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        Patient patient = patientRepository.fetchActivePatientById(id)
                .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patient;
    }

    @Override
    public PatientDetailResponseDTO search(PatientSearchRequestDTO searchRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO responseDTO = patientRepository.search(searchRequestDTO);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<PatientMinimalResponseDTO> responseDTOS = patientRepository.fetchMinimalPatientInfo
                (searchRequestDTO, pageable);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public PatientDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO responseDTO = patientRepository.fetchDetailsById(id);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;

    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository.fetchDropDownList(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoActiveDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository.fetchActiveDropDownList(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }


//    @Override
//    public void deletePatient(DeleteRequestDTO deleteRequestDTO) {
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(DELETING_PROCESS_STARTED, PATIENT);
////
////        deletePatient.apply(fetchPatientById(deleteRequestDTO.getId()), deleteRequestDTO);
////
////        log.info(DELETING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
//    }
//
//    @Override
//    public void updatePatient(PatientUpdateRequestDTO updateRequestDTO) {
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(UPDATING_PROCESS_STARTED, PATIENT);
////
////        Patient patientToUpdate = fetchPatientById(updateRequestDTO.getId());
////
////        validatePatientByCode(updateRequestDTO.getId(), updateRequestDTO.getCode());
////
////        parseToUpdate(patientToUpdate, updateRequestDTO);
////
////        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
//    }
//
//
//    @Override
//    public List<PatientMinimalResponseDTO> searchPatient(PatientSearchRequestDTO searchDTO, Pageable pageable) {
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(SEARCHING_PROCESS_STARTED, PATIENT);
////
////        List<PatientMinimalResponseDTO> minimalResponseDTOS = patientRepository
////                .searchPatient(searchDTO, pageable);
////
////        minimalResponseDTOS.forEach(patientMinimalResponseDTO -> {
////            getAge(patientMinimalResponseDTO);
////        });
////
////        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
////
////        return minimalResponseDTOS;
//        return null;
//    }
//
//    @Override
//    public PatientDetailResponseDTO fetchPatientDetails(Long id) {
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(FETCHING_DETAIL_PROCESS_STARTED, PATIENT);
////
////        PatientResponseDTO responseDTO = patientRepository.fetchPatientDetailsById(id);
////
////        responseDTO.setAge(convertDateToAge(responseDTO.getDateOfBirth()));
////
////        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
////    return null;
////        return responseDTO;
//        return null;
//    }
//
//    @Override
//    public List<DropDownResponseDTO> dropDownList() {
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PATIENT);
////
////        List<DropDownResponseDTO> dropDownResponseDTOS = patientRepository.fetchDropDownList()
////                .orElseThrow(() -> new NoContentFoundException(Patient.class));
////
////        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
////
////        return dropDownResponseDTOS;
//        return null;
//    }
//
//    @Override
//    public List<DropDownResponseDTO> activeDropDownList() {
//
////        Long startTime = getTimeInMillisecondsFromLocalDate();
////
////        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PATIENT);
////
////        List<DropDownResponseDTO> dropDownResponseDTOS = patientRepository.fetchActiveDropDownList()
////                .orElseThrow(() -> new NoContentFoundException(Patient.class));
////
////        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
////
////        return dropDownResponseDTOS;
//        return null;
//    }

    private void validatePatientDuplicity(Long patientCount, String name, String mobileNumber,
                                          Date dateOfBirth) {

        if (patientCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE, name, mobileNumber, utilDateToSqlDate(dateOfBirth)));
    }

    private Gender fetchGender(Character genderCode) {
        return GenderUtils.fetchGenderByCode(genderCode);
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    public Patient savePatient(PatientRequestDTO requestDTO) {

        Gender gender = fetchGender(requestDTO.getGender());
        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        return save(PatientUtils.parseToPatient(requestDTO, gender, hospital));
    }

    private Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    private Function<Long, NoContentFoundException> PATIENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Patient.class, "id", id.toString());
    };

    public Patient fetchPatientById(Long id) {
        return patientRepository.fetchPatientById(id).orElseThrow(() ->
                new NoContentFoundException(Patient.class));
    }

//    public PatientMinimalResponseDTO getAge(PatientMinimalResponseDTO minimalResponseDTO) {
//        String age = convertDateToAge(minimalResponseDTO.getDateOfBirth());
//        minimalResponseDTO.setAge(age);
//        return minimalResponseDTO;
//    }

    public String convertDateToAge(LocalDate birthdayDate) {
        return AgeConverterUtils.ageConverter(birthdayDate);
    }
}

