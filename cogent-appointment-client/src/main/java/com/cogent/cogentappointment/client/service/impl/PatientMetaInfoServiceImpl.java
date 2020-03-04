package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.client.service.PatientMetaInfoService;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.PATIENT_META_INFO;
import static com.cogent.cogentappointment.client.utils.PatientMetaInfoUtils.parseToPatientMetaInfo;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        PatientMetaInfo patientMetaInfo = parseToPatientMetaInfo(patient);

        savePatientMetaInfo(patientMetaInfo);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT_META_INFO, getDifferenceBetweenTwoTime(startTime));

    }

    private void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }
}
