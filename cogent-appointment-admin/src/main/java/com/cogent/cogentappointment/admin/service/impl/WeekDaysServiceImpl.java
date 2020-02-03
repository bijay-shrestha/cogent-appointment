package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.WeekDays;
import com.cogent.cogentappointment.admin.repository.WeekDaysRepository;
import com.cogent.cogentappointment.admin.service.WeekDaysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 25/11/2019
 */
@Service
@Transactional
@Slf4j
public class WeekDaysServiceImpl implements WeekDaysService {

    private final WeekDaysRepository weekDaysRepository;

    public WeekDaysServiceImpl(WeekDaysRepository weekDaysRepository) {
        this.weekDaysRepository = weekDaysRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveWeekDays() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, WEEK_DAYS);

        List<DropDownResponseDTO> responseDTOS = weekDaysRepository.fetchActiveWeekDays();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public WeekDays fetchWeekDaysById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, WEEK_DAYS);

        WeekDays weekDays = weekDaysRepository.fetchActiveWeekDaysById(id)
                .orElseThrow(() -> new NoContentFoundException(WeekDays.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return weekDays;
    }
}
