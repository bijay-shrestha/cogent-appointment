package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.model.WeekDays;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
public interface WeekDaysService {
    List<DropDownResponseDTO> fetchActiveWeekDays();

    WeekDays fetchWeekDaysById(Long id);
}
