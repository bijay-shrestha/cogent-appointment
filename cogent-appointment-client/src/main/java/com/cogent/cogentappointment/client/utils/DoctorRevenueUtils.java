package com.cogent.cogentappointment.client.utils;


import com.cogent.cogentappointment.client.dto.request.DoctorRevenueRequestDTO;

/**
 * @author Rupak
 */
public class DoctorRevenueUtils {

    public static DoctorRevenueRequestDTO convertToDoctorRevenueRequestDTO(Long doctorId,
                                                                           Long hospitalId,
                                                                           Long specializationId) {

        DoctorRevenueRequestDTO doctorRevenueRequestDTO = DoctorRevenueRequestDTO.builder()
                .doctorId(doctorId)
                .hospitalId(hospitalId)
                .specializationId(specializationId)
                .build();
        return doctorRevenueRequestDTO;

    }
}
