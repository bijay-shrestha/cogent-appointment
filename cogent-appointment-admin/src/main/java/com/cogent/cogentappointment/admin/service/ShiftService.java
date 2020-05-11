package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Shift;

import java.util.List;

/**
 * @author smriti on 06/05/20
 */
public interface ShiftService {

    List<DropDownResponseDTO> fetchShiftByHospitalId(Long hospitalId);

    Shift fetchActiveShiftByIdAndHospitalId(Long id, Long hospitalId);

    String fetchNameByIds(String shiftIds);
}
