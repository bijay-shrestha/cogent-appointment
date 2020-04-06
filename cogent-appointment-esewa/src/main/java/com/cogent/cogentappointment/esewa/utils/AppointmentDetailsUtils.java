package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.*;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.AvailableDatesResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDates;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static java.lang.reflect.Array.get;
import static org.springframework.http.HttpStatus.OK;


/**
 * @author smriti on 15/03/20
 */
public class AppointmentDetailsUtils {
    public static List<AvailableDoctorWithSpecialization> mergeOverrideAndActualDoctorList(
            List<AvailableDoctorWithSpecialization> overrideList,
            List<AvailableDoctorWithSpecialization> actualList) {

        List<AvailableDoctorWithSpecialization> unmatchedList = actualList.stream()
                .filter(actual -> (overrideList.stream()
                        .filter(override -> (override.getDoctorId().equals(actual.getDoctorId()))
                                && (override.getSpecializationId().equals(actual.getSpecializationId()))
                                && (actual.getDayOffStatus().equals(NO))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        overrideList.addAll(unmatchedList);

        overrideList.removeIf(override -> override.getDayOffStatus().equals(YES));

        return overrideList;
    }

    public static AppointmentDatesResponseDTO getFinalResponse(AppointmentDatesRequestDTO requestDTO,
                                                               List<AvailableDatesResponseDTO> finalAvaliableDateAndTime) {
        AppointmentDatesResponseDTO appointmentDatesResponseDTO = new AppointmentDatesResponseDTO();
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());
        appointmentDatesResponseDTO.setDates(finalAvaliableDateAndTime);
        appointmentDatesResponseDTO.setResponseCode(OK.value());
        appointmentDatesResponseDTO.setResponseStatus(OK);

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
        } else {
            return avaliableRosterDates;
        }
        return unmatched;
    }

    public static List<Date> getDutyRosterDates(List<Date> dates,
                                                List<String> weekDays) {
        List<Date> availableDates = new ArrayList<>();
        for (Date date : dates) {
            weekDays.forEach(weekdays -> {
                if (date.toString().substring(0, 3).toUpperCase().equals(weekdays)) {
                    availableDates.add(utilDateToSqlDate(date));
                }
            });
        }
        return availableDates;
    }

    public static AvailableDoctorWithSpecializationResponseDTO getAvailableDoctorWithSpecializationResponseDTO(
            List<AvailableDoctorWithSpecialization> mergedList) {

        return AvailableDoctorWithSpecializationResponseDTO.builder()
                .availableDoctorWithSpecializations(mergedList)
                .responseCode(OK.value())
                .responseStatus(OK)
                .build();
    }

    public static AvailableDatesWithSpecializationResponseDTO getAvailableDatesWithSpecializationResponseDTO(
            List<AvailableDatesWithSpecialization> responseDTOList) {

        return AvailableDatesWithSpecializationResponseDTO.builder()
                .availableDatesWithSpecialization(responseDTOList)
                .responseCode(OK.value())
                .responseStatus(OK)
                .build();
    }

    public static AvailableDatesWithDoctorResponseDTO getAvailableDatesWithDoctorResponseDTO(
            List<AvailableDatesWithDoctor> responseDTOList) {

        return AvailableDatesWithDoctorResponseDTO.builder()
                .availableDatesWithDoctor(responseDTOList)
                .responseCode(OK.value())
                .responseStatus(OK)
                .build();
    }

    public static void parseDoctorAvailabilityResponseStatus(DoctorAvailabilityStatusResponseDTO responseDTO) {
        responseDTO.setResponseStatus(OK);
        responseDTO.setResponseCode(OK.value());
    }

    public static DoctorAvailabilityStatusResponseDTO parseToDoctorAvailabilityStatusResponseDTO(Object[] queryResult) {
        String doctorName = (String) get(queryResult, 0);
        Character dayOffStatus = (Character) get(queryResult, 1);

        if (dayOffStatus.equals(NO))
            return DoctorAvailabilityStatusResponseDTO.builder()
                    .status("Y")
                    .message(doctorName + " IS AVAILABLE FOR THE DAY.")
                    .responseCode(OK.value())
                    .responseStatus(OK)
                    .build();
        else
            return DoctorAvailabilityStatusResponseDTO.builder()
                    .status("N")
                    .message(doctorName + " IS NOT AVAILABLE FOR THE DAY.")
                    .responseCode(OK.value())
                    .responseStatus(OK)
                    .build();
    }

}
