package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;

/**
 * @author smriti on 15/03/20
 */
public class eSewaUtils {

    public static DoctorAvailabilityStatusResponseDTO parseToDoctorAvailabilityStatusResponseDTO(Character status) {

        return DoctorAvailabilityStatusResponseDTO.builder()
                .status(status)
                .build();

    }
}
