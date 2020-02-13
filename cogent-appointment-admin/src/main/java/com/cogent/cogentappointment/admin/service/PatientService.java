package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti ON 16/01/2020
 */
public interface PatientService {

    List<DropDownResponseDTO> patientMetaInfoDropDownListByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> patientMetaInfoActiveDropDownListByHospitalId(Long hospitalId);

    void registerPatient(Long patientId);
}
