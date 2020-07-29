package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentWithStatusResponseDTO;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 08/06/20
 */
public class HospitalDepartmentUtils {

    public static HospitalDepartmentWithStatusResponseDTO parseHospitalDepartmentDetails(
            List<HospitalDepartmentResponseDTO> departmentInfo) {

        HospitalDepartmentWithStatusResponseDTO responseDTO = new HospitalDepartmentWithStatusResponseDTO();
        responseDTO.setDepartmentInfo(departmentInfo);
        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
        return responseDTO;
    }
}
