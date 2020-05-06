package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 06/05/20
 */
public interface ShiftService {

    List<DropDownResponseDTO> fetchShiftByHospitalId(Long hospitalId);
}
