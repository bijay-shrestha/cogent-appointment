package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.*;
import com.cogent.cogentappointment.client.dto.response.eSewa.*;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.client.service.EsewaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.eSewaLog.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.eSewaUtils.*;

@Service
@Transactional
@Slf4j
public class EsewaServiceImpl implements EsewaService {

    private final DoctorDutyRosterRepository dutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository;

    public EsewaServiceImpl(DoctorDutyRosterRepository dutyRosterRepository,
                            DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository) {
        this.dutyRosterRepository = dutyRosterRepository;
        this.dutyRosterOverrideRepository = dutyRosterOverrideRepository;
    }

    @Override
    public AppointmentDatesResponseDTO fetchDoctorAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES_AND_TIME);

        List<AvaliableDatesResponseDTO> apoointmentDateAndTime = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        for (DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate : doctorDutyRosterAppointmentDates) {

            List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate =
                    getWeekdaysTimeByRosterId(doctorDutyRosterAppointmentDate.getId());

            List<Date> dates = getDates(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate());

            List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO =
                    getDutyRosterDatesAndTime(dates, weekDaysDutyRosterAppointmentDate);

            checkIfOverrideExists(doctorDutyRosterAppointmentDate, appointmentDatesResponseDTO, apoointmentDateAndTime);
        }

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES_AND_TIME, getDifferenceBetweenTwoTime(startTime));

        return getFinalResponse(requestDTO, apoointmentDateAndTime);
    }

    /*RETURN MESSAGE IF DOCTOR IS AVAILABLE ON DATE*/
    @Override
    public DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_STATUS);

        DoctorAvailabilityStatusResponseDTO doctorAvailableStatus =
                dutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        DoctorAvailabilityStatusResponseDTO responseDTO =
                doctorAvailableStatus.getStatus().equals("Y")
                        ? doctorAvailableStatus
                        : dutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_STATUS, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AvailableDoctorResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_DOCTOR_LIST);

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_DOCTOR_LIST, getDifferenceBetweenTwoTime(startTime));

        return null;
    }

    @Override
    public List<AvaliableDateByDoctorIdResponseDTO> fetchDoctorAvailableDates(Long doctorId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES);

        List<AvaliableDateByDoctorIdResponseDTO> responseDTOList = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDateAndSpecilizationDTO> appointmentDateAndSpecilizations = dutyRosterRepository
                .getAvaliableDatesAndSpecilizationByDoctorId(doctorId);

        appointmentDateAndSpecilizations.forEach(DateAndSpecilization -> {

            AvaliableDateByDoctorIdResponseDTO responseDTO = new AvaliableDateByDoctorIdResponseDTO();

            List<Date> availableDates = getDates(DateAndSpecilization.getFromDate(), DateAndSpecilization.getToDate());

            responseDTO.setSpecializationId(DateAndSpecilization.getSpecilizationId());

            responseDTO.setSpecilaizationName(DateAndSpecilization.getSpecilizationName());

            if (DateAndSpecilization.getHasOverride().equals('Y')) {

                List<DutyRosterOverrideAppointmentDate> dateList = dutyRosterOverrideRepository
                        .fetchDayOffRosterOverridebyRosterId(DateAndSpecilization.getId());

                dateList.forEach(date -> {

                    List<Date> dayOffDates = getDates(date.getFromDate(), date.getToDate());

                    responseDTO.setAvaliableDates(mergeRosterAndRosterOverrideDates(availableDates, dayOffDates));

                    responseDTOList.add(responseDTO);
                });
            } else {
                responseDTO.setAvaliableDates(availableDates);

                responseDTOList.add(responseDTO);
            }
        });

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }

    private List<AvaliableDatesResponseDTO> getOverrideDatesAndTime(
            DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        final List<Date> dates = new ArrayList<>();

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterOverrideAppointmentDate> appointmentDatesAndTime =
                getDateAndTimeFromOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());

        appointmentDatesAndTime.forEach(appointmentDate -> {

            getAllOverrideDates(appointmentDate, dates);

            for (Date date : dates) {

                AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();

                datesResponseDTO.setDate(utilDateToSqlDate(date));

                checkIfDayOff(appointmentDate, datesResponseDTO, avaliableDates);
            }
        });
        return avaliableDates;
    }

    private List<AvaliableDatesResponseDTO> getDutyRosterDatesAndTime(List<Date> dates,
                                                                      List<DoctorWeekDaysDutyRosterAppointmentDate>
                                                                              weekDaysDutyRosterAppointmentDate) {

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        for (Date date : dates) {

            AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();

            weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {
                getAllDutyRosterDatesAndTime(date, weekdays, datesResponseDTO, avaliableDates);
            });
        }
        return avaliableDates;
    }

    private List<DoctorDutyRosterOverrideAppointmentDate> getDateAndTimeFromOverrideByRosterId(Long rosterId) {
        return dutyRosterOverrideRepository
                .getRosterOverrideByRosterId(rosterId);
    }

    private List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekdaysTimeByRosterId(Long dutyRosterId) {
        return dutyRosterRepository
                .getWeekDaysDutyRosterByDutyRosterId(dutyRosterId);
    }

    private void checkIfOverrideExists(DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate,
                                       List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO,
                                       List<AvaliableDatesResponseDTO> apoointmentDateAndTime) {
        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

            List<AvaliableDatesResponseDTO> avaliableDatesResponseDTOS =
                    getOverrideDatesAndTime(doctorDutyRosterAppointmentDate);

            List<AvaliableDatesResponseDTO> datesResponseDTO = mergeRosterAndRosterOverrideDatesAndTime(
                    appointmentDatesResponseDTO,
                    avaliableDatesResponseDTOS);

            getAllDateAndTime(apoointmentDateAndTime, datesResponseDTO);

        } else {

            getAllDateAndTime(apoointmentDateAndTime, appointmentDatesResponseDTO);

        }
    }

}


