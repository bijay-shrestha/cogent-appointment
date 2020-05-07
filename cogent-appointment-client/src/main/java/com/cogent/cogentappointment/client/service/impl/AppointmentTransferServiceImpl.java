package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.client.repository.AppointmentTransferRepository;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.*;
import static com.cogent.cogentappointment.client.utils.AppointmentTransferUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Service
@Slf4j
@Transactional
public class AppointmentTransferServiceImpl implements AppointmentTransferService {

    private final AppointmentTransferRepository repository;

    public AppointmentTransferServiceImpl(AppointmentTransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Date> fetchAvailableDatesByDoctorId(Long doctorId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<Date> actualDate = new ArrayList<>();

        List<Date> overrideDate = new ArrayList<>();

        List<DoctorDatesResponseDTO> rosterDates = repository.getDatesByDoctorId(doctorId);

        rosterDates.forEach(rosterId -> {

            List<Date> dates = getActualdate(repository.getDayOffDaysByRosterId(rosterId.getId()),
                    getDates(rosterId.getFromDate(), rosterId.getToDate()));

            actualDate.addAll(dates);
        });

        List<DoctorDatesResponseDTO> overrideDates = repository.getOverrideDatesByDoctorId(doctorId);

        overrideDates.forEach(date -> {
            overrideDate.addAll(getDates(date.getFromDate(), date.getToDate()));
        });

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return utilDateListToSqlDateList(mergeOverrideAndActualDateList(overrideDate, actualDate));
    }

    @Override
    public List<String> fetchAvailableDoctorTime(AppointmentTransferTimeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<String> time = new ArrayList<>();

        List<ActualDateAndTimeResponseDTO> actualDateAndTime = repository.getActualTimeByDoctorId(requestDTO.getDoctorId());

        for (ActualDateAndTimeResponseDTO actualResponse : actualDateAndTime) {
            if (actualResponse.getHasOverride().equals(YES)) {
                List<OverrideDateAndTimeResponseDTO> overrideDateAndTime = repository.getOverrideTimeByRosterId(actualResponse.getId());
                for (OverrideDateAndTimeResponseDTO overrideResponse : overrideDateAndTime) {
                    List<Date> dates = getDates(overrideResponse.getFromDate(), overrideResponse.getToDate());
                    Date responseDate = (Date) dates.stream()
                            .filter(date -> date.equals(requestDTO.getDate()));
                    List<String> unavailableTime = repository.getUnavailableTimeByDateAndDoctorId(
                            requestDTO.getDoctorId(),
                            responseDate);

                    List<String> overrideTime = getGapDuration(overrideResponse.getStartTime(),
                            overrideResponse.getEndTime(),
                            actualResponse.getGapDuration());
                    time = getVacantTime(overrideTime, unavailableTime);
                }
            } else {
                String code = requestDTO.getDate().toString().substring(0, 3);
                WeekDayAndTimeDTO codeAndTime = repository.getWeekDaysByCode(requestDTO.getDoctorId(), code);
                List<String> unavailableTime = repository.getUnavailableTimeByDateAndDoctorId(
                        requestDTO.getDoctorId(),
                        requestDTO.getDate());
                List<String> actualTime = getGapDuration(codeAndTime.getStartTime(),
                        codeAndTime.getEndTime(),
                        actualResponse.getGapDuration());
                time= getVacantTime(actualTime, unavailableTime);
            }
        }

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return time;
    }


}
