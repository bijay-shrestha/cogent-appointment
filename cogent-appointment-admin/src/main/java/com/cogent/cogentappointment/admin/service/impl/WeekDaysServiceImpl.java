package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.model.WeekDays;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.log.constants.WeekDaysLog;
import com.cogent.cogentappointment.admin.repository.WeekDaysRepository;
import com.cogent.cogentappointment.admin.service.WeekDaysService;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, WeekDaysLog.WEEK_DAYS);

        List<DropDownResponseDTO> responseDTOS = weekDaysRepository.fetchActiveWeekDays();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, WeekDaysLog.WEEK_DAYS, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public WeekDays fetchWeekDaysById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, WeekDaysLog.WEEK_DAYS);

        WeekDays weekDays = weekDaysRepository.fetchActiveWeekDaysById(id)
                .orElseThrow(() -> new NoContentFoundException(WeekDays.class, "id", id.toString()));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, WeekDaysLog.WEEK_DAYS, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return weekDays;
    }
}
