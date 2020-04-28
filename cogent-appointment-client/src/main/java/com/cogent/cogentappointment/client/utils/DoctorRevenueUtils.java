package com.cogent.cogentappointment.client.utils;


import com.cogent.cogentappointment.client.dto.request.DoctorRevenueRequestDTO;

import java.text.ParseException;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertStringToDate;

/**
 * @author Rupak
 */
public class DoctorRevenueUtils {

    public static DoctorRevenueRequestDTO convertToDoctorRevenueRequestDTO(Long doctorId,
                                                                           Long hospitalId,
                                                                           Long specializationId,
                                                                           String fromDate,
                                                                           String toDate) throws ParseException {

       return DoctorRevenueRequestDTO.builder()
                .doctorId(doctorId)
                .hospitalId(hospitalId)
                .specializationId(specializationId)
                .fromDate(convertStringToDate(fromDate))
                .toDate(convertStringToDate(toDate))
                .build();
    }
}
