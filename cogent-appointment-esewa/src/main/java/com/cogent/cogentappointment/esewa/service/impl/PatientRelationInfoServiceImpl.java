package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.repository.PatientRelationInfoRepository;
import com.cogent.cogentappointment.esewa.service.PatientRelationInfoService;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.DELETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.PATIENT_RELATION_INFO;
import static com.cogent.cogentappointment.esewa.utils.PatientRelationInfoUtils.parseToPatientRelationInfo;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 01/03/20
 */
@Service
@Transactional
@Slf4j
public class PatientRelationInfoServiceImpl implements PatientRelationInfoService {

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    public PatientRelationInfoServiceImpl(PatientRelationInfoRepository patientRelationInfoRepository) {
        this.patientRelationInfoRepository = patientRelationInfoRepository;
    }

    @Override
    public void savePatientRelationInfo(Patient parentPatient,
                                        Patient childPatient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PATIENT_RELATION_INFO);

        PatientRelationInfo patientRelationInfo = fetchPatientRelationInfo(
                parentPatient.getId(), childPatient.getId());

        if (Objects.isNull(patientRelationInfo))
            save(parseToPatientRelationInfo(parentPatient, childPatient));
        else
            updatePatientRelationInfoStatus(patientRelationInfo);

        log.info(SAVING_PROCESS_COMPLETED, PATIENT_RELATION_INFO, getDifferenceBetweenTwoTime(startTime));
    }

    private void updatePatientRelationInfoStatus(PatientRelationInfo patientRelationInfo) {
        if (patientRelationInfo.getStatus().equals(DELETED))
            patientRelationInfo.setStatus(ACTIVE);
    }

    private PatientRelationInfo fetchPatientRelationInfo(Long parentPatientId,
                                                         Long childPatientId) {

        return patientRelationInfoRepository.fetchPatientRelationInfo(parentPatientId, childPatientId);
    }

    private void save(PatientRelationInfo patientRelationInfo) {
        patientRelationInfoRepository.save(patientRelationInfo);
    }
}
