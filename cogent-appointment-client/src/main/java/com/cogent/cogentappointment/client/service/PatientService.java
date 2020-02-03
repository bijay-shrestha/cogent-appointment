package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {
    Patient save(PatientRequestDTO requestDTO, Long hospitalId);

    Patient fetchPatient(Long id);

    PatientDetailResponseDTO search(PatientSearchRequestDTO searchRequestDTO);

    List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);

    PatientDetailResponseDTO fetchDetailsById(Long id);

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
