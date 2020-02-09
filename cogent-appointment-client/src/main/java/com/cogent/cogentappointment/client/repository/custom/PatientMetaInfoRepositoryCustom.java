package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
@Qualifier("patientMetaInfoRepositoryCustom")
public interface PatientMetaInfoRepositoryCustom {

    List<DropDownResponseDTO> fetchMinPatientMetaInfo(Long hospitalId);

    List<DropDownResponseDTO> fetchActiveMinPatientMetaInfo(Long hospitalId);
}
