package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.admin.utils.PatientUtils.registerPatientDetails;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientMetaInfoRepository patientMetaInfoRepository,
                              HospitalPatientInfoRepository hospitalPatientInfoRepository,
                              PatientRepository patientRepository) {
        this.patientMetaInfoRepository = patientMetaInfoRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.patientRepository = patientRepository;
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

    @Override
    public void registerPatient(Long patientId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REGISTERING_PATIENT_PROCESS_STARTED);

        HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NoContentFoundException(Patient.class, "patientId", patientId.toString()));

        String latestRegistrationNumber =
                patientRepository.fetchLatestRegistrationNumber();

        registerPatientDetails(hospitalPatientInfo, latestRegistrationNumber);

        log.info(REGISTERING_PATIENT_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

    }
}

