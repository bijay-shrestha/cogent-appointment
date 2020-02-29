package com.cogent.cogentappointment.client.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Qualifier("hospitalPatientInfoRepositoryCustom")
public interface HospitalPatientInfoRepositoryCustom {

    Long fetchHospitalPatientInfoCount(Long patientId,
                                       Long hospitalId);
}
