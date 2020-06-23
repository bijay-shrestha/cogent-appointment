package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.HospitalDepartmentRevenueRequestDTO;

import java.text.ParseException;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertStringToDate;

/**
 * @author Sauravi Thapa
 */
public class HospitalDepartmentRevenueUtils {

    public static HospitalDepartmentRevenueRequestDTO convertToHospitalDepartmentRevenueRequestDTO(Long hospitalDepartmentId,
                                                                           Long hospitalId,
                                                                           String fromDate,
                                                                           String toDate) throws ParseException {
        return HospitalDepartmentRevenueRequestDTO.builder()
                .hospitalDepartmentId(hospitalDepartmentId)
                .hospitalId(hospitalId)
                .fromDate(convertStringToDate(fromDate))
                .toDate(convertStringToDate(toDate))
                .build();
    }
}
