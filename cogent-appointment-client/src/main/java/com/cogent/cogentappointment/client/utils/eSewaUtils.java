package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AvailableDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDates;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;


/**
 * @author smriti on 15/03/20
 */
public class eSewaUtils {

    public static DoctorAvailabilityStatusResponseDTO parseToDoctorAvailabilityStatusResponseDTO(Character status) {
        return null;
    }

    public static AppointmentDatesResponseDTO getFinalResponse(AppointmentDatesRequestDTO requestDTO,
                                                               List<AvailableDatesResponseDTO> finalAvaliableDateAndTime) {
        AppointmentDatesResponseDTO appointmentDatesResponseDTO = new AppointmentDatesResponseDTO();
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());
        appointmentDatesResponseDTO.setDates(finalAvaliableDateAndTime);

        return appointmentDatesResponseDTO;
    }

    public static void getAllDateAndTime(List<AvailableDatesResponseDTO> appointmentDateAndTime,
                                         List<AvailableDatesResponseDTO> availableDatesResponseDTOS) {
        appointmentDateAndTime.addAll(availableDatesResponseDTOS);

    }

    public static void getAllDate(List<Date> finalList,
                                  List<Date> availableDatesResponseDTOS) {
        finalList.addAll(availableDatesResponseDTOS);

    }

    public static List<AvailableDatesResponseDTO> mergeRosterAndRosterOverrideDatesAndTime(
            List<AvailableDatesResponseDTO> avaliableRosterDates,
            List<AvailableDatesResponseDTO> avaliableRosterOverrideDates) {
        List<AvailableDatesResponseDTO> finalDateAndTimeResponse = new ArrayList<>();
        if (avaliableRosterOverrideDates != null && !avaliableRosterOverrideDates.isEmpty()) {
            List<AvailableDatesResponseDTO> unmatched = avaliableRosterDates.stream()
                    .filter(dates -> avaliableRosterOverrideDates.stream()
                            .filter(overrideDate -> overrideDate.getDate().equals(dates.getDate()))
                            .count() < 1)
                    .collect(Collectors.toList());
            finalDateAndTimeResponse.addAll(unmatched);
            List<AvailableDatesResponseDTO> matched = avaliableRosterOverrideDates.stream()
                    .filter(overrideDates -> avaliableRosterOverrideDates.stream()
                            .filter(dates -> overrideDates.getDate().equals(dates.getDate())
                                    && !overrideDates.getDoctorAvailableTime().equals("12:00-12:00"))
                            .count() > 0)
                    .collect(Collectors.toList());
            finalDateAndTimeResponse.addAll(matched);
            finalDateAndTimeResponse.sort(Comparator.comparing(AvailableDatesResponseDTO::getDate));
        } else {
            return avaliableRosterDates;
        }
        return finalDateAndTimeResponse;
    }

    public static void checkIfDayOff(DoctorDutyRosterOverrideAppointmentDate appointmentDate,
                                     AvailableDatesResponseDTO avaliableDate,
                                     List<AvailableDatesResponseDTO> avaliableDates) {
        if (appointmentDate.getDayOff().equals('Y')) {
            avaliableDate.setDoctorAvailableTime("12:00-12:00");
        } else {
            avaliableDate.setDoctorAvailableTime(appointmentDate.getStartTime() + "-" + appointmentDate.getEndTime());
        }
        avaliableDates.add(avaliableDate);
    }

    public static void getAllDutyRosterDatesAndTime(Date date, DoctorWeekDaysDutyRosterAppointmentDate weekdays,
                                                    AvailableDatesResponseDTO datesResponseDTO,
                                                    List<AvailableDatesResponseDTO> avaliableDates) {
        if (date.toString().substring(0, 3).toUpperCase().equals(weekdays.getWeekDay())) {
            datesResponseDTO.setDate(utilDateToSqlDate(date));
            datesResponseDTO.setDoctorAvailableTime(weekdays.getStartTime() + "-" + weekdays.getEndTime());
            avaliableDates.add(datesResponseDTO);
        }
    }

    public static void getAllOverrideDates(DoctorDutyRosterOverrideAppointmentDate appointmentDate, List<Date> dates) {
        if (!appointmentDate.getFromDate().equals(appointmentDate.getToDate())) {
            dates.addAll(getDates(appointmentDate.getFromDate(),
                    appointmentDate.getToDate()));
        } else {
            dates.add(appointmentDate.getFromDate());
        }
    }

    public static List<Date> mergeRosterAndRosterOverrideDates(
            List<Date> avaliableRosterDates,
            List<Date> avaliableRosterOverrideDates) {

        List<Date> unmatched = new ArrayList<>();
        if (avaliableRosterOverrideDates != null && !avaliableRosterOverrideDates.isEmpty()) {
            unmatched = avaliableRosterDates.stream()
                    .filter(dates -> avaliableRosterOverrideDates.stream()
                            .filter(overrideDate -> overrideDate.equals(dates))
                            .count() < 1)
                    .collect(Collectors.toList());
            unmatched.sort(Comparator.comparing(Date::getDate));
        } else {
            return avaliableRosterDates;
        }
        return unmatched;
    }

    public static void getAllDutyRosterDates(Date date,
                                             String weekName,
                                             List<Date> dates) {
        if (date.toString().substring(0, 3).toUpperCase().equals(weekName)) {
            dates.add(utilDateToSqlDate(date));
        }
    }
}
