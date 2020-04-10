package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.patient.*;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTOWithStatus;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientResponseDTOForOthersWithStatus;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    Patient saveSelfPatient(PatientRequestByDTO requestDTO);

    Patient saveOtherPatient(PatientRequestForDTO requestForPatientInfo);

    Patient fetchPatientById(Long id);

    PatientDetailResponseDTOWithStatus searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    Patient fetchPatient(PatientRequestForDTO patientRequestForDTO);


    /*FETCH MINIMAL DETAILS OF 'OTHERS'*/
    PatientResponseDTOForOthersWithStatus searchForOthers(PatientMinSearchRequestDTO searchRequestDTO,
                                                          Pageable pageable);

    PatientDetailResponseDTO fetchMinPatientDetailsOfOthers(Long hospitalPatientId);

    void updateOtherPatientDetails(PatientUpdateDTOForOthers requestDTO);

    void deleteOtherPatient(PatientDeleteRequestDTOForOthers requestDTO);

}
