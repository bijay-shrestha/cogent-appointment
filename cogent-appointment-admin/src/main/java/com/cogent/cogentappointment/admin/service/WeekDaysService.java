package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.response.weekDays.WeekDaysMinResponseDTO;
import com.cogent.cogentappointment.admin.model.WeekDays;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
public interface WeekDaysService {
    List<WeekDaysMinResponseDTO> fetchActiveWeekDays();

    WeekDays fetchWeekDaysById(Long id);
}
