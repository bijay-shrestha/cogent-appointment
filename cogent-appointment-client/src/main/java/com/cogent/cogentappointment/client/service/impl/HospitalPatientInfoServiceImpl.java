package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.service.HospitalPatientInfoService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.HOSPITAL_PATIENT_INFO;
import static com.cogent.cogentappointment.client.utils.PatientUtils.parseHospitalPatientInfo;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 29/02/20
 */
@Service
@Transactional
@Slf4j
public class HospitalPatientInfoServiceImpl implements HospitalPatientInfoService {

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    public HospitalPatientInfoServiceImpl(HospitalPatientInfoRepository hospitalPatientInfoRepository) {
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
    }

    @Override
    public void saveHospitalPatientInfo(Hospital hospital,
                                        Patient patient,
                                        Character isSelf,
                                        String email,
                                        String address) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_PATIENT_INFO);

        Long hospitalPatientInfoCount = fetchHospitalPatientInfoCount(patient.getId(), hospital.getId());

        if (hospitalPatientInfoCount.intValue() <= 0) {
            HospitalPatientInfo hospitalPatientInfo = parseHospitalPatientInfo(
                    hospital, patient, isSelf, email, address
            );

            saveHospitalPatientInfo(hospitalPatientInfo);
        }

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_PATIENT_INFO, getDifferenceBetweenTwoTime(startTime));
    }

    private Long fetchHospitalPatientInfoCount(Long patientId, Long hospitalId) {
        return hospitalPatientInfoRepository.fetchHospitalPatientInfoCount(patientId, hospitalId);
    }

    private void saveHospitalPatientInfo(HospitalPatientInfo hospitalPatientInfo) {
        hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }
}
