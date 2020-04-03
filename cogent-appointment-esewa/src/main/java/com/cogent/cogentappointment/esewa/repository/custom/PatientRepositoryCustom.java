package com.cogent.cogentappointment.esewa.repository.custom;


import com.cogent.cogentappointment.esewa.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.*;
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

    /*appointmentDetails*/
    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    List<PatientRelationInfoResponseDTO> fetchPatientRelationInfo(PatientMinSearchRequestDTO searchRequestDTO);

    PatientResponseDTOForOthers fetchMinPatientInfoForOthers(List<PatientRelationInfoResponseDTO> patientRelationInfo,
                                                             Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    /*admin*/
    Long validatePatientDuplicity(Long patientId, String name, String mobileNumber, Date dateOfBirth);

    PatientResponseDTO fetchPatientDetailsById(Long id, Long hospitalId);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable, Long hospitalId);


    String fetchLatestRegistrationNumber(Long hospitalId);

    Patient fetchPatient(String name, String mobileNumber, Date dateOfBirth);

    PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);

    Patient getPatientByHospitalPatientInfoId(Long hospitalPatientInfoId, Long hospitalId);
}
