package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Qualifier("patientRelationInfoRepositoryCustom")
public interface PatientRelationInfoRepositoryCustom {

    PatientRelationInfo fetchPatientRelationInfo(Long parentPatientId,
                                                 Long childPatientId);

}
