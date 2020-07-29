package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.weekdays.WeekDaysResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.WeekDaysRepository;
import com.cogent.cogentappointment.client.service.WeekDaysService;
import com.cogent.cogentappointment.persistence.model.WeekDays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.WeekDaysLog.WEEK_DAYS;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
                .orElseThrow(() -> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return weekDays;
    }

    @Override
    public List<WeekDaysResponseDTO> fetchPrepareWeekDays() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, WEEK_DAYS);

        List<WeekDaysResponseDTO> responseDTOS = weekDaysRepository.fetchPrepareWeekDays();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, WEEK_DAYS, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private Function<Long, NoContentFoundException> WEEK_DAYS_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,WEEK_DAYS, id);
        throw new NoContentFoundException(WeekDays.class, "id", id.toString());
    };
}
