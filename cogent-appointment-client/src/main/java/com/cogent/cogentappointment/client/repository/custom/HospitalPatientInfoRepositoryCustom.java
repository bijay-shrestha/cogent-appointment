package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Qualifier("hospitalPatientInfoRepositoryCustom")
public interface HospitalPatientInfoRepositoryCustom {

    Long fetchHospitalPatientInfoCount(Long patientId, Long hospitalId);

    HospitalPatientInfo fetchHospitalPatientInfo(Long patientId, Long hospitalId);
}
