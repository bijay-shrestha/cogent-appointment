package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentDateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.DoctorChargeRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentTransferRepository;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
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
    public List<Date> fetchAvailableDatesByDoctorId(AppointmentDateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        List<Date> actualDate = new ArrayList<>();

        List<Date> overrideDate = new ArrayList<>();

        List<DoctorDatesResponseDTO> rosterDates = repository.getDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        rosterDates.forEach(rosterId -> {

            List<Date> dates = getActualdate(repository.getDayOffDaysByRosterId(rosterId.getId()),
                    getDates(rosterId.getFromDate(), rosterId.getToDate()));

            actualDate.addAll(dates);
        });

        List<DoctorDatesResponseDTO> overrideDates = repository.getOverrideDatesByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

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

        List<String> unavailableTime = repository.getUnavailableTimeByDateAndDoctorId(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getDate());

        Date sqlRequestDate = utilDateToSqlDate(requestDTO.getDate());

        time.addAll(checkOverride(requestDTO,unavailableTime,sqlRequestDate));

        if (time.size() == 0) {
           time.addAll(checkActual(requestDTO,unavailableTime,sqlRequestDate));
        }

        log.info(FETCHING_AVAILABLE_DOCTOR_TIME_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return time;
    }

    @Override
    public Double fetchDoctorChargeByDoctorId(DoctorChargeRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_STARTED, APPOINTMENT_TRANSFER);

        AppointmentChargeResponseDTO charge = repository.getAppointmentChargeByDoctorId(requestDTO.getDoctorId());

        Double response = requestDTO.getFollowUp().equals(NO) ? charge.getActualCharge() : charge.getFollowUpCharge();

        log.info(FETCHING_DOCTOR_CHARGE_PROCESS_COMPLETED, APPOINTMENT_TRANSFER,
                getDifferenceBetweenTwoTime(startTime));

        return response;
    }


    private List<String> checkOverride(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                       Date sqlRequestDate) {

        List<String> finalOverridetime = new ArrayList<>();

        List<OverrideDateAndTimeResponseDTO> overrideDateAndTime = repository.getOverideRosterDateAndTime(
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        for (OverrideDateAndTimeResponseDTO override : overrideDateAndTime) {

            List<Date> overrideDates = utilDateListToSqlDateList(getDates(override.getFromDate(),
                    override.getToDate()));

            Date date = compareAndGetDate(overrideDates, sqlRequestDate);

            if (date != null) {
                List<String> overrideTime = getGapDuration(override.getStartTime(),
                        override.getEndTime(),
                        override.getGapDuration());

                finalOverridetime = getVacantTime(overrideTime, unavailableTime, date);
            }
        }
        return finalOverridetime;
    }


    private List<String> checkActual(AppointmentTransferTimeRequestDTO requestDTO, List<String> unavailableTime,
                                       Date sqlRequestDate) {

        List<String> finaltime = new ArrayList<>();

        List<ActualDateAndTimeResponseDTO> actualDateAndTime = repository.getActualTimeByDoctorId(requestDTO.getDoctorId(),
                requestDTO.getSpecializationId());

        for (ActualDateAndTimeResponseDTO actual : actualDateAndTime) {
            List<Date> actualDates = utilDateListToSqlDateList(getDates(actual.getFromDate(),
                    actual.getToDate()));

            for (Date actualDate : actualDates) {

                if (actualDate.equals(sqlRequestDate)) {

                    String code = requestDTO.getDate().toString().substring(0, 3);

                    WeekDayAndTimeDTO codeAndTime = repository.getWeekDaysByCode(requestDTO.getDoctorId(), code);

                    List<String> actualTime = getGapDuration(codeAndTime.getStartTime(), codeAndTime.getEndTime(),
                            actual.getGapDuration());

                    finaltime = getVacantTime(actualTime, unavailableTime, actualDate);
                }
            }
        }
        return finaltime;
    }


}
