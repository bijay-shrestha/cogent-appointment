package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.*;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientSearchResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    Patient saveSelfPatient(PatientRequestByDTO requestDTO, Hospital hospital);

    Patient saveOtherPatient(PatientRequestByDTO requestByPatientInfo,
                             PatientRequestForDTO requestForPatientInfo,
                             Hospital hospital);

    Patient fetchPatientById(Long id);

    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    /*FETCH MINIMAL DETAILS OF 'OTHERS'*/
    List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientMinSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);


    PatientResponseDTO fetchDetailsById(Long id);

    List<PatientSearchResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                          Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    List<DropDownResponseDTO> fetchMinPatientMetaInfo();

    List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo();

    void registerPatient(Long patientId, Long hospitalId);
}
