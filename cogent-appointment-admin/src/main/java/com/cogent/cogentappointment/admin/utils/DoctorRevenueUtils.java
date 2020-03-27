package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;

/**
 * @author Rupak
 */
public class DoctorRevenueUtils {

    public static DoctorRevenueRequestDTO convertToDoctorRevenueRequestDTO(Long doctorId,
                                                                           Long hospitalId,
                                                                           Long specializationId) {
        return DoctorRevenueRequestDTO.builder()
                .doctorId(doctorId)
                .hospitalId(hospitalId)
                .specializationId(specializationId)
                .build();
    }
}
