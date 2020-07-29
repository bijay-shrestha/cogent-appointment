package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.esewa.service.PatientMetaInfoService;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.PATIENT_META_INFO;
import static com.cogent.cogentappointment.esewa.utils.PatientMetaInfoUtils.parseToPatientMetaInfo;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 01/03/20
 */
@Service
@Transactional
@Slf4j
public class PatientMetaInfoServiceImpl implements PatientMetaInfoService {

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    public PatientMetaInfoServiceImpl(PatientMetaInfoRepository patientMetaInfoRepository) {
        this.patientMetaInfoRepository = patientMetaInfoRepository;
    }

    @Override
    public void savePatientMetaInfo(Patient patient) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT_META_INFO);

        PatientMetaInfo patientMetaInfo = fetchPatientMetaInfo(patient.getId());

        if (Objects.isNull(patientMetaInfo))
            savePatientMetaInfo(parseToPatientMetaInfo(patient));

        log.info(SAVING_PROCESS_COMPLETED, PATIENT_META_INFO, getDifferenceBetweenTwoTime(startTime));
    }

    private void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }

    private PatientMetaInfo fetchPatientMetaInfo(Long patientId) {
        return patientMetaInfoRepository.fetchByPatientId(patientId);
    }
}
