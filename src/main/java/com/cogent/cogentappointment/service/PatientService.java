package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.model.Patient;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {
    Patient save(PatientRequestDTO requestDTO);

    Patient fetchPatient(Long id);

    PatientDetailResponseDTO search(PatientSearchRequestDTO searchRequestDTO);

//    List<PatientDetailResponseDTO> fetchPatientDetails(PatientSearchRequestDTO searchRequestDTO);
//
//    void deletePatient(DeleteRequestDTO deleteRequestDTO);
//
//    List<PatientMinimalResponseDTO> searchPatient(PatientSearchRequestDTO searchDTO, Pageable pageable);
//
//    PatientDetailResponseDTO fetchPatientDetails(Long id);
//
//    List<DropDownResponseDTO> dropDownList();
//
//    List<DropDownResponseDTO> activeDropDownList();
//
//    void updatePatient(PatientUpdateRequestDTO updateRequestDTO);


}
