package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.model.WeekDays;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
public interface WeekDaysService {
    List<DropDownResponseDTO> fetchActiveWeekDays();

    WeekDays fetchWeekDaysById(Long id);
}
