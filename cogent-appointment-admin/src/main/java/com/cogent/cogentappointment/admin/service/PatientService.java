package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.admin.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {
    Patient save(PatientRequestDTO requestDTO);

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
