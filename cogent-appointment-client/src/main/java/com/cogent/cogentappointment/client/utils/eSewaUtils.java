package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AvaliableDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDatesBetween;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

public class eSewaUtils {
    public static AppointmentDatesResponseDTO getFinalResponse(AppointmentDatesRequestDTO requestDTO,
                                                               List<AvaliableDatesResponseDTO> finalAvaliableDateAndTime) {
        AppointmentDatesResponseDTO appointmentDatesResponseDTO = new AppointmentDatesResponseDTO();
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());
        appointmentDatesResponseDTO.setDates(finalAvaliableDateAndTime);

        return appointmentDatesResponseDTO;
    }

    public static void getAllDateAndTime(List<AvaliableDatesResponseDTO> apoointmentDateAndTime,
                                         List<AvaliableDatesResponseDTO> test) {
        apoointmentDateAndTime.addAll(test);

    }

    public static List<AvaliableDatesResponseDTO> mergeRosterAndRosterOverride(
            List<AvaliableDatesResponseDTO> avaliableRosterDates,
            List<AvaliableDatesResponseDTO> avaliableRosterOverrideDates) {
        List<AvaliableDatesResponseDTO> finalDateAndTimeResponse = new ArrayList<>();
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
        } else {
            return avaliableRosterDates;
        }
        return finalDateAndTimeResponse;
    }

    public static void checkIfDayOff(DoctorDutyRosterOverrideAppointmentDate appointmentDate,
                               AvaliableDatesResponseDTO avaliableDate,
                               List<AvaliableDatesResponseDTO> avaliableDates) {
        if (appointmentDate.getDayOff().equals('Y')) {
            avaliableDate.setDoctorAvailableTime("12:00-12:00");
        } else {
            avaliableDate.setDoctorAvailableTime(appointmentDate.getStartTime() + "-" + appointmentDate.getEndTime());
        }
        avaliableDates.add(avaliableDate);
    }

    public static void getAllDutyRosterDatesAndTime(Date date, DoctorWeekDaysDutyRosterAppointmentDate weekdays,
                                              AvaliableDatesResponseDTO datesResponseDTO,
                                              List<AvaliableDatesResponseDTO> avaliableDates) {
        if (date.toString().substring(0, 3).toUpperCase().equals(weekdays.getWeekDay())) {
            datesResponseDTO.setDate(utilDateToSqlDate(date));
            datesResponseDTO.setDoctorAvailableTime(weekdays.getStartTime() + "-" + weekdays.getEndTime());
            avaliableDates.add(datesResponseDTO);
        }
    }

    public static void getAllOverrideDates(DoctorDutyRosterOverrideAppointmentDate appointmentDate, List<Date> dates) {
        if (!appointmentDate.getFromDate().equals(appointmentDate.getToDate())) {
            dates.addAll(getDatesBetween(appointmentDate.getFromDate(),
                    appointmentDate.getToDate()));
        } else {
            dates.add(appointmentDate.getFromDate());
        }
    }
}
