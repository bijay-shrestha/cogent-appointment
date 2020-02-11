package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.admin.utils.PatientUtils.updatePatient;
import static com.cogent.cogentappointment.admin.utils.PatientUtils.updatePatientMetaInfo;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMetaInfoRepository patientMetaInfoRepository) {
        this.patientRepository = patientRepository;
        this.patientMetaInfoRepository = patientMetaInfoRepository;
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
    public List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        List<PatientResponseDTO> responseDTO = patientRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void update(PatientUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PATIENT);

        Patient patientToBeUpdated = fetchPatientById(updateRequestDTO.getId());

        Long patientCount = patientRepository.validatePatientDuplicity(updateRequestDTO);

        validatePatientDuplicity(patientCount, updateRequestDTO.getName(),
                updateRequestDTO.getMobileNumber(), updateRequestDTO.getDateOfBirth());

        save(updatePatient(updateRequestDTO, patientToBeUpdated));

        PatientMetaInfo patientMetaInfoToBeUpdated = patientMetaInfoRepository.fetchByPatientId(updateRequestDTO.getId());

        savePatientMetaInfo(updatePatientMetaInfo(patientToBeUpdated, patientMetaInfoToBeUpdated, updateRequestDTO));

        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository
                .fetchPatientMetaInfoDropDownListByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoActiveDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository
                .fetchActivePatientMetaInfoDropDownListByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }


    public Patient fetchPatientById(Long id) {
        return patientRepository.fetchPatientById(id).orElseThrow(() ->
                new NoContentFoundException(Patient.class));
    }

    private Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }

    private void validatePatientDuplicity(Long patientCount, String name, String mobileNumber,
                                          Date dateOfBirth) {

        if (patientCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(DUPLICATE_PATIENT_MESSAGE,
                            name,
                            mobileNumber,
                            utilDateToSqlDate(dateOfBirth)));
    }
}

