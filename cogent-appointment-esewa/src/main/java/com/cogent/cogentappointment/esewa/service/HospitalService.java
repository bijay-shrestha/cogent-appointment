package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTOWithStatus;
import com.cogent.cogentappointment.persistence.model.Hospital;

/**
 * @author smriti ON 12/01/2020
 */
public interface HospitalService {

    Hospital fetchActiveHospital(Long id);

    HospitalMinResponseDTOWithStatus fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO);
}
