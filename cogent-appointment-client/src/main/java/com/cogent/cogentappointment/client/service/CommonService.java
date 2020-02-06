package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.response.common.DoctorSpecializationResponseDTO;

/**
 * @author smriti ON 05/02/2020
 */
public interface CommonService {
    DoctorSpecializationResponseDTO fetchDoctorSpecializationInfo(Long hospitalId);
}
