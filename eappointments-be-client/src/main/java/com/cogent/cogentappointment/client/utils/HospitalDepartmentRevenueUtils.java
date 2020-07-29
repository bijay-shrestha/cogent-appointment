package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.dashboard.HospitalDepartmentRevenueRequestDTO;

import java.text.ParseException;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertStringToDate;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertStringToDate;

/**
 * @author Sauravi Thapa
 */
public class HospitalDepartmentRevenueUtils {

    public static HospitalDepartmentRevenueRequestDTO convertToHospitalDepartmentRevenueRequestDTO(Long hospitalDepartmentId,
                                                                           String fromDate,
                                                                           String toDate) throws ParseException {
        return HospitalDepartmentRevenueRequestDTO.builder()
                .hospitalDepartmentId(hospitalDepartmentId)
                .fromDate(convertStringToDate(fromDate))
                .toDate(convertStringToDate(toDate))
                .build();
    }
}
