package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.response.weekDays.WeekDaysMinResponseDTO;
import com.cogent.cogentappointment.client.model.WeekDays;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
public interface WeekDaysService {
    List<WeekDaysMinResponseDTO> fetchActiveWeekDays();

    WeekDays fetchWeekDaysById(Long id);
}
