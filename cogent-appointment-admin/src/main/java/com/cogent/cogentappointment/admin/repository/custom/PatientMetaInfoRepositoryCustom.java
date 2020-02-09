package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Qualifier("patientMetaInfoRepositoryCustom")
public interface PatientMetaInfoRepositoryCustom {
    List<DropDownResponseDTO> fetchPatientMetaInfoDropDownListByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchActivePatientMetaInfoDropDownListByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchPatientMetaInfoDropDownList();

    List<DropDownResponseDTO> fetchActivePatientMetaInfoDropDownList();

}
