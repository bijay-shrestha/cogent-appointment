package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTOWithStatus;
import com.cogent.cogentappointment.persistence.model.Hospital;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
public interface HospitalService {
    Hospital fetchActiveHospital(Long id);

    HospitalMinResponseDTOWithStatus fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO);
}
