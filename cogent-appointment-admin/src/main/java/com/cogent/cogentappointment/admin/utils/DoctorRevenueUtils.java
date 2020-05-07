package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;

import java.text.ParseException;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertStringToDate;

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
