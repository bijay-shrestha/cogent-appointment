package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 28/05/20
 */
public interface HospitalDepartmentService {

    List<DropDownResponseDTO> fetchActiveMinHospitalDepartment(Long hospitalId);

}
