package com.cogent.cogentappointment.esewa.repository.custom;


import com.cogent.cogentappointment.esewa.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientRelationInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientResponseDTOForOthers;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
@Qualifier("patientRepositoryCustom")
public interface PatientRepositoryCustom {

    Long validatePatientDuplicity(Long patientId, String name, String mobileNumber, Date dateOfBirth);

    /*appointmentDetails*/
    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    List<PatientRelationInfoResponseDTO> fetchPatientRelationInfo(PatientMinSearchRequestDTO searchRequestDTO);

    PatientResponseDTOForOthers fetchMinPatientInfoForOthers(List<PatientRelationInfoResponseDTO> patientRelationInfo,
                                                             Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    Patient fetchPatient(String name, String mobileNumber, Date dateOfBirth);


}
