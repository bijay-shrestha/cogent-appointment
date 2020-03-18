package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.*;
import com.cogent.cogentappointment.client.dto.response.patient.*;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    /*eSewa*/
    Patient saveSelfPatient(PatientRequestByDTO requestDTO);

    Patient saveOtherPatient(PatientRequestForDTO requestForPatientInfo);

    Patient fetchPatient(PatientRequestForDTO patientRequestForDTO);

    Patient fetchPatientById(Long id);

    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    /*FETCH MINIMAL DETAILS OF 'OTHERS'*/
    PatientResponseDTOForOthers searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    void updateOtherPatientDetails(PatientUpdateDTOForOthers requestDTO);

    void deleteOtherPatient(PatientDeleteRequestDTOForOthers requestDTO);

    /*admin*/
    PatientResponseDTO fetchDetailsById(Long id);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    List<DropDownResponseDTO> fetchMinPatientMetaInfo();

    List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo();

    void registerPatient(Long patientId, Long hospitalId);

    PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);
}
