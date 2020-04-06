package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.*;
import com.cogent.cogentappointment.esewa.dto.response.patient.*;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    /*appointmentDetails*/
    Patient saveSelfPatient(PatientRequestByDTO requestDTO);

    Patient saveOtherPatient(PatientRequestForDTO requestForPatientInfo);

    Patient fetchPatient(PatientRequestForDTO patientRequestForDTO);

    Patient fetchPatientById(Long id);

    PatientDetailResponseDTOWithStatus searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    /*FETCH MINIMAL DETAILS OF 'OTHERS'*/
    PatientResponseDTOForOthersWithStatus searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    void updateOtherPatientDetails(PatientUpdateDTOForOthers requestDTO);

    void deleteOtherPatient(PatientDeleteRequestDTOForOthers requestDTO);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    void registerPatient(Long patientId, Long hospitalId);
}
