package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.*;
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

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDatesBetween;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

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

        System.out.println(dates);

        List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO =
                getDutyRosterDateAndTime(dates, weekDaysDutyRosterAppointmentDate);

        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {
            List<AvaliableDatesResponseDTO> avaliableDatesResponseDTOS =
                    getDutyRosterOverrideDates(doctorDutyRosterAppointmentDate);
            return merge(requestDTO, appointmentDatesResponseDTO, avaliableDatesResponseDTOS);
        }
        return merge(requestDTO, appointmentDatesResponseDTO, null);
    }

    private List<AvaliableDatesResponseDTO> getDutyRosterOverrideDates(
            DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        DoctorDutyRosterOverrideAppointmentDate appointmentDatesAndTime = dutyRosterOverrideRepository
                .getRosterOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());
        List<Date> dates = new ArrayList<>();
        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        if (!appointmentDatesAndTime.getFromDate().equals(appointmentDatesAndTime.getToDate())) {
            dates = getDatesBetween(appointmentDatesAndTime.getFromDate(),
                    appointmentDatesAndTime.getToDate());
        } else {
            dates.add(appointmentDatesAndTime.getFromDate());
        }

        for (Date date : dates) {
            AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();
            datesResponseDTO.setDate(utilDateToSqlDate(date));
            datesResponseDTO.setDoctorAvailableTime(
                    appointmentDatesAndTime.getStartTime() +
                            "-" +
                            appointmentDatesAndTime.getEndTime());
            avaliableDates.add(datesResponseDTO);
        }
        return avaliableDates;
    }

    private List<AvaliableDatesResponseDTO> getDutyRosterDateAndTime(List<Date> dates,
                                                                     List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate) {

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
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());
        if (avaliableRosterOverrideDates.size() != 0) {
            List<AvaliableDatesResponseDTO> unmatchDates = avaliableRosterDates.stream()
                    .filter(dates -> avaliableRosterOverrideDates.stream()
                            .filter(overrideDate -> overrideDate.getDate().equals(dates.getDate()))
                            .count() < 1)
                    .collect(Collectors.toList());
            avaliableRosterOverrideDates.addAll(unmatchDates);
            avaliableRosterOverrideDates.sort(Comparator.comparing(AvaliableDatesResponseDTO::getDate));
            appointmentDatesResponseDTO.setDates(avaliableRosterOverrideDates);
        } else {
            appointmentDatesResponseDTO.setDates(avaliableRosterDates);
        }
        return appointmentDatesResponseDTO;
    }
}

