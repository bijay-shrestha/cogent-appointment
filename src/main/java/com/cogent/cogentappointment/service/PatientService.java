package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {
    void save(PatientRequestDTO requestDTO);

    void deletePatient(DeleteRequestDTO deleteRequestDTO);

    List<PatientMinimalResponseDTO> searchPatient(PatientSearchRequestDTO searchDTO, Pageable pageable);

    PatientResponseDTO fetchPatientDetails(Long id);

    List<DropDownResponseDTO> dropDownList();

    List<DropDownResponseDTO> activeDropDownList();

    void updatePatient(PatientUpdateRequestDTO updateRequestDTO);

    Patient fetchPatient(Long id);
}
