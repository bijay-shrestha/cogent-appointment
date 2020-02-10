package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {
    Patient save(PatientRequestDTO requestDTO, Long hospitalId);

    Patient fetchActivePatientById(Long id);

    PatientDetailResponseDTO searchForSelf(PatientMinSearchRequestDTO searchRequestDTO);

    /*FETCH MINIMAL DETAILS OF 'OTHERS'*/
    List<PatientMinimalResponseDTO> fetchMinimalPatientInfo(PatientMinSearchRequestDTO searchRequestDTO,
                                                            Pageable pageable);

    /*FETCH DETAILS OF 'OTHERS'*/
    PatientDetailResponseDTO fetchDetailsById(Long id);

    List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                    Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    List<DropDownResponseDTO> fetchMinPatientMetaInfo();

    List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo();
}
