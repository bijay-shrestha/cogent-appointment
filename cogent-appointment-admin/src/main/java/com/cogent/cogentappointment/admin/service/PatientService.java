package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    PatientDetailResponseDTO fetchDetailsById(Long id);

    List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO,
                                    Pageable pageable);

    void update(PatientUpdateRequestDTO patientUpdateRequestDTO);

    List<DropDownResponseDTO> patientMetaInfoDropDownListByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> patientMetaInfoActiveDropDownListByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> patientMetaInfoDropDownList();

    List<DropDownResponseDTO> patientMetaInfoActiveDropDownList();
}
