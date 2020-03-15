package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.*;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.client.service.EsewaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.eSewaLog.DOCTOR_AVAILABLE_STATUS;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

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
    public AppointmentDatesResponseDTO doctorAvailableTime(AppointmentDatesRequestDTO requestDTO) {
        DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate = dutyRosterRepository
                .getWeekDaysDutyRosterByDutyRosterId(doctorDutyRosterAppointmentDate.getId());

        List<Date> dates = getDatesBetween(doctorDutyRosterAppointmentDate.getFromDate(),
                doctorDutyRosterAppointmentDate.getToDate());

        List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO =
                getDutyRosterDateAndTime(dates, weekDaysDutyRosterAppointmentDate);

        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

            List<AvaliableDatesResponseDTO> avaliableDatesResponseDTOS =
                    getDutyRosterOverrideDates(doctorDutyRosterAppointmentDate);

            AppointmentDatesResponseDTO datesResponseDTO = merge(requestDTO, appointmentDatesResponseDTO,
                    avaliableDatesResponseDTOS);

            return datesResponseDTO;
        }
        return merge(requestDTO, appointmentDatesResponseDTO, null);
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

    private List<AvaliableDatesResponseDTO> getDutyRosterOverrideDates(
            DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        List<DoctorDutyRosterOverrideAppointmentDate> appointmentDatesAndTime = dutyRosterOverrideRepository
                .getRosterOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());
        final List<Date> dates = new ArrayList<>();
        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        appointmentDatesAndTime.forEach(appointmentDate -> {
            if (!appointmentDate.getFromDate().equals(appointmentDate.getToDate())) {
                dates.addAll(getDatesBetween(appointmentDate.getFromDate(),
                        appointmentDate.getToDate()));
            } else {
                dates.add(appointmentDate.getFromDate());
            }
            for (Date date : dates) {
                AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();
                datesResponseDTO.setDate(utilDateToSqlDate(date));
                datesResponseDTO.setDoctorAvailableTime(
                        appointmentDate.getStartTime() +
                                "-" +
                                appointmentDate.getEndTime());
                avaliableDates.add(datesResponseDTO);
            }
        });
        return avaliableDates;
    }

    private List<AvaliableDatesResponseDTO> getDutyRosterDateAndTime(List<Date> dates,
                                                                     List<DoctorWeekDaysDutyRosterAppointmentDate>
                                                                             weekDaysDutyRosterAppointmentDate) {

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        for (Date date : dates) {
            AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();
            weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {
                if (date.toString().substring(0, 3).toUpperCase().equals(weekdays.getWeekDay())) {
                    datesResponseDTO.setDate(utilDateToSqlDate(date));
                    datesResponseDTO.setDoctorAvailableTime(weekdays.getStartTime() + "-" + weekdays.getEndTime());
                    avaliableDates.add(datesResponseDTO);
                }
            });
        }
        return avaliableDates;
    }

    private AppointmentDatesResponseDTO merge(AppointmentDatesRequestDTO requestDTO,
                                              List<AvaliableDatesResponseDTO> avaliableRosterDates,
                                              List<AvaliableDatesResponseDTO> avaliableRosterOverrideDates) {
        AppointmentDatesResponseDTO appointmentDatesResponseDTO = new AppointmentDatesResponseDTO();
        List<AvaliableDatesResponseDTO> finalDateAndTimeResponse = new ArrayList<>();
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());
        if (avaliableRosterOverrideDates != null && !avaliableRosterOverrideDates.isEmpty()) {
            List<AvaliableDatesResponseDTO> unmatched = avaliableRosterDates.stream()
                    .filter(dates -> avaliableRosterOverrideDates.stream()
                            .filter(overrideDate -> overrideDate.getDate().equals(dates.getDate()))
                            .count() < 1)
                    .collect(Collectors.toList());
            finalDateAndTimeResponse.addAll(unmatched);
            List<AvaliableDatesResponseDTO> matched = avaliableRosterOverrideDates.stream()
                    .filter(overrideDates -> avaliableRosterOverrideDates.stream()
                            .filter(dates -> overrideDates.getDate().equals(dates.getDate())
                                    && !overrideDates.getDoctorAvailableTime().equals("12:00-12:00"))
                            .count() > 0)
                    .collect(Collectors.toList());
            finalDateAndTimeResponse.addAll(matched);
            finalDateAndTimeResponse.sort(Comparator.comparing(AvaliableDatesResponseDTO::getDate));
            appointmentDatesResponseDTO.setDates(finalDateAndTimeResponse);
        } else {
            appointmentDatesResponseDTO.setDates(avaliableRosterDates);
        }
        return appointmentDatesResponseDTO;
    }
}

