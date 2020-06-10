package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.response.common.DoctorSpecializationResponseDTO;

/**
 * @author smriti ON 05/02/2020
 */
public interface CommonService {

    DoctorSpecializationResponseDTO fetchDoctorSpecializationInfo(Long hospitalId);

}
