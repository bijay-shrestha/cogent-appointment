package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    PatientResponseDTO fetchDetailsById(Long id);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    List<DropDownResponseDTO> fetchMinPatientMetaInfo();

    List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo();

    void registerPatient(Long patientId, Long hospitalId);

    PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);

    List<DropDownResponseDTO> fetchPatientEsewaId();
}
