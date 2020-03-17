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

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES AND TIME BY DOCTORID and SPECIALIZATIONID*/
    @Override
    public AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES_AND_TIME);

        List<AvailableDatesResponseDTO> appointmentDateAndTime = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        for (DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate : doctorDutyRosterAppointmentDates) {

            List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate =
                    getWeekdaysTimeByRosterId(doctorDutyRosterAppointmentDate.getId());

            List<Date> dates = getDates(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate());

            List<AvailableDatesResponseDTO> appointmentDatesResponseDTO =
                    getDutyRosterDatesAndTime(dates, weekDaysDutyRosterAppointmentDate);

            checkIfOverrideExists(doctorDutyRosterAppointmentDate, appointmentDatesResponseDTO, appointmentDateAndTime);
        }

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES_AND_TIME, getDifferenceBetweenTwoTime(startTime));

        return getFinalResponse(requestDTO, appointmentDateAndTime);
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

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES WITH SPECIALIZATION ID AND NAME BY DOCTORID*/
    @Override
    public List<AvailableDateByDoctorIdResponseDTO> fetchAvailableDatesWithSpecialization(Long doctorId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES);

        List<AvailableDateByDoctorIdResponseDTO> responseDTOList = new ArrayList<>();

        List<DutyRosterAppointmentDateAndSpecilizationDTO> appointmentDateAndSpecilizations = dutyRosterRepository
                .getAvaliableDatesAndSpecilizationByDoctorId(doctorId);

        appointmentDateAndSpecilizations.forEach(dateAndSpecilization -> {

            AvailableDateByDoctorIdResponseDTO responseDTO = new AvailableDateByDoctorIdResponseDTO();

            List<Date> availableDates = utilDateListToSqlDateList(getDates(dateAndSpecilization.getFromDate(),
                    dateAndSpecilization.getToDate()));

            if (availableDates.size() != 0) {

                responseDTO.setSpecializationId(dateAndSpecilization.getSpecializationId());

                responseDTO.setSpecilaizationName(dateAndSpecilization.getSpecializationName());

                if (dateAndSpecilization.getHasOverride().equals('Y')) {
                    List<DutyRosterOverrideAppointmentDate> dateList = dutyRosterOverrideRepository
                            .fetchDayOffRosterOverridebyRosterId(dateAndSpecilization.getId());

                    dateList.forEach(date -> {

                        List<Date> dayOffDates = utilDateListToSqlDateList(getDates(date.getFromDate(), date.getToDate()));

                        responseDTO.setAvaliableDates(mergeRosterAndRosterOverrideDates(availableDates, dayOffDates));
                    });
                } else {
                    responseDTO.setAvaliableDates(availableDates);
                }
                responseDTOList.add(responseDTO);
            }
        });

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES WITH DOCTOR ID AND NAME BY SPECIALIZATIONID*/
    @Override
    public List<AvailableDateBySpecializationIdResponseDTO> fetchAvailableDatesWithDoctor(Long specializationId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, SPECIALIZATION_AVAILABLE_DATES);

        List<AvailableDateBySpecializationIdResponseDTO> responseDTOList = new ArrayList<>();

        List<DutyRosterAppointmentDateAndDoctorDTO> dateAndDoctorDTOS = dutyRosterRepository
                .getAvaliableDatesAndDoctorBySpecilizationId(specializationId);

        dateAndDoctorDTOS.forEach(dateAndSpecilization -> {

            AvailableDateBySpecializationIdResponseDTO responseDTO = new AvailableDateBySpecializationIdResponseDTO();

            List<Date> availableDates = utilDateListToSqlDateList(getDates(dateAndSpecilization.getFromDate(),
                    dateAndSpecilization.getToDate()));

            if (availableDates.size() != 0) {

                responseDTO.setDoctorId(dateAndSpecilization.getDoctorId());

                responseDTO.setDoctorName(dateAndSpecilization.getDoctorName());

                if (dateAndSpecilization.getHasOverride().equals('Y')) {
                    List<DutyRosterOverrideAppointmentDate> dateList = dutyRosterOverrideRepository
                            .fetchDayOffRosterOverridebyRosterId(dateAndSpecilization.getId());

                    dateList.forEach(date -> {

                        List<Date> dayOffDates = utilDateListToSqlDateList(getDates(date.getFromDate(), date.getToDate()));

                        responseDTO.setAvaliableDates(mergeRosterAndRosterOverrideDates(availableDates, dayOffDates));
                    });
                } else {
                    responseDTO.setAvaliableDates(availableDates);
                }
                responseDTOList.add(responseDTO);
            }
        });

        log.info(FETCHING_PROCESS_COMPLETED, SPECIALIZATION_AVAILABLE_DATES, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }


    @Override
    public AvailableDoctorResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_DOCTOR_LIST);

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_DOCTOR_LIST, getDifferenceBetweenTwoTime(startTime));

        return null;
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES  BY DOCTORID AND SPECIALIZATIONID*/
    @Override
    public AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_DATES_LIST);

        AllAvailableDatesResponseDTO responseDTO = new AllAvailableDatesResponseDTO();

        List<Date> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        doctorDutyRosterAppointmentDates.forEach(doctorDutyRosterAppointmentDate -> {

            List<Date> availableRosterDates = utilDateListToSqlDateList(getDates(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate()));

            if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

                List<DoctorDutyRosterOverrideAppointmentDate> overrideAppointmentDates =
                        getDateAndTimeFromOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());

                overrideAppointmentDates.forEach(date -> {
                    List<Date> dayOffDates = utilDateListToSqlDateList(getDates(date.getFromDate(), date.getToDate()));
                    if (dayOffDates.size() != 0) {
                        getAllDate(avaliableDates, mergeRosterAndRosterOverrideDates(availableRosterDates, dayOffDates));
                    }
                });
            } else {
                getAllDate(avaliableDates, availableRosterDates);
            }
        });
        responseDTO.setAvaliableDates(avaliableDates);

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_DATES_LIST, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


    private List<AvailableDatesResponseDTO> getOverrideDatesAndTime(
            DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        final List<Date> dates = new ArrayList<>();

        List<AvailableDatesResponseDTO> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterOverrideAppointmentDate> appointmentDatesAndTime =
                getDateAndTimeFromOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());

        appointmentDatesAndTime.forEach(appointmentDate -> {

            getAllOverrideDates(appointmentDate, dates);

            for (Date date : dates) {

                AvailableDatesResponseDTO datesResponseDTO = new AvailableDatesResponseDTO();

                datesResponseDTO.setDate(utilDateToSqlDate(date));

                checkIfDayOff(appointmentDate, datesResponseDTO, avaliableDates);
            }
        });
        return avaliableDates;
    }

    private List<AvailableDatesResponseDTO> getDutyRosterDatesAndTime(List<Date> dates,
                                                                      List<DoctorWeekDaysDutyRosterAppointmentDate>
                                                                              weekDaysDutyRosterAppointmentDate) {

        List<AvailableDatesResponseDTO> availableDates = new ArrayList<>();

        for (Date date : dates) {

            AvailableDatesResponseDTO datesResponseDTO = new AvailableDatesResponseDTO();

            weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {

                getAllDutyRosterDatesAndTime(date, weekdays, datesResponseDTO, availableDates);

            });
        }
        return availableDates;
    }

    private List<DoctorDutyRosterOverrideAppointmentDate> getDateAndTimeFromOverrideByRosterId(Long rosterId) {
        return dutyRosterOverrideRepository
                .getRosterOverrideByRosterId(rosterId);
    }

    private List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekdaysTimeByRosterId(Long dutyRosterId) {
        return dutyRosterRepository.getWeekDaysDutyRosterByDutyRosterId(dutyRosterId);
    }

    private void checkIfOverrideExists(DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate,
                                       List<AvailableDatesResponseDTO> appointmentDatesResponseDTO,
                                       List<AvailableDatesResponseDTO> apoointmentDateAndTime) {
        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

            List<AvailableDatesResponseDTO> availableDatesResponseDTOS =
                    getOverrideDatesAndTime(doctorDutyRosterAppointmentDate);

            List<AvailableDatesResponseDTO> datesResponseDTO = mergeRosterAndRosterOverrideDatesAndTime(
                    appointmentDatesResponseDTO,
                    availableDatesResponseDTOS);

            getAllDateAndTime(apoointmentDateAndTime, datesResponseDTO);

        } else {

            getAllDateAndTime(apoointmentDateAndTime, appointmentDatesResponseDTO);

        }
    }

}


