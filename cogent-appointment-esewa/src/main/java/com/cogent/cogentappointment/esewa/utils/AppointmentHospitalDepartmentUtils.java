package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.availableDate.AppointmentAvailableDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.HospitalDepartmentDoctorInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 29/05/20
 */
public class AppointmentHospitalDepartmentUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithRoom(
            Date queryDate,
            List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomList,
            HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo,
            String startTime,
            String endTime,
            List<String> availableTimeSlots,
            List<HospitalDepartmentDoctorInfoResponseDTO> availableDoctors) {

        AppointmentHospitalDeptCheckAvailabilityResponseDTO responseDTO =
                AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                        .queryDate(queryDate)
                        .hasRoom(YES)
                        .roomInfo(availableRoomList)
                        .hospitalDepartmentRoomInfoId(roomInfo.getHospitalDepartmentRoomInfoId())
                        .roomNumber(roomInfo.getRoomNumber())
                        .hospitalDepartmentAvailableTime(startTime + HYPHEN + endTime)
                        .availableTimeSlots(availableTimeSlots)
                        .availableDoctors(availableDoctors)
                        .build();

        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
        return responseDTO;
    }

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithoutRoom(
            Date queryDate,
            String startTime,
            String endTime,
            List<String> availableTimeSlots,
            List<HospitalDepartmentDoctorInfoResponseDTO> availableDoctors) {

        AppointmentHospitalDeptCheckAvailabilityResponseDTO responseDTO =
                AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                        .queryDate(queryDate)
                        .hasRoom(NO)
                        .roomInfo(new ArrayList<>())
                        .hospitalDepartmentAvailableTime(startTime + HYPHEN + endTime)
                        .availableTimeSlots(availableTimeSlots)
                        .availableDoctors(availableDoctors)
                        .build();

        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
        return responseDTO;
    }

    public static AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO parseToAvailabilityResponseRoomWise(
            Date queryDate,
            String roomNumber,
            String startTime,
            String endTime,
            List<String> availableTimeSlots) {

        AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO responseDTO =
                AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO.builder()
                        .queryDate(queryDate)
                        .roomNumber(roomNumber)
                        .hospitalDepartmentAvailableTime(startTime + HYPHEN + endTime)
                        .availableTimeSlots(availableTimeSlots)
                        .build();

        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
        return responseDTO;
    }

    public static List<String> calculateAvailableTimeSlots(String startTime,
                                                           String endTime,
                                                           Duration rosterGapDuration,
                                                           List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                           Date requestedDate) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if ((!isAppointmentDateMatched(bookedAppointments, date)) && (!hasTimePassed(requestedDate, date)))
                availableTimeSlots.add(date);

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
    }

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getAppointmentTime().equals(date));
    }

    private static boolean hasTimePassed(Date date, String time) {

        Date availableDateTime = parseAppointmentTime(date, time);

        Date currentDate = new Date();

        return availableDateTime.before(currentDate);
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

    /*VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN HOSPITAL DEPARTMENT DUTY ROSTER TIME SCHEDULES
 * IF IT MATCHES, THEN DO NOTHING
 * ELSE REQUESTED TIME IS INVALID AND THUS CANNOT TAKE AN APPOINTMENT*/
    public static boolean validateIfRequestedAppointmentTimeIsValid(
            HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO,
            String appointmentTime) {

        final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

        String startTime = getTimeFromDate(dutyRosterTimeResponseTO.getStartTime());
        String endTime = getTimeFromDate(dutyRosterTimeResponseTO.getEndTime());

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            final Duration rosterGapDuration = Minutes.minutes(dutyRosterTimeResponseTO.getRosterGapDuration())
                    .toStandardDuration();

            if (date.equals(appointmentTime))
                return true;

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return false;
    }

    public static AppointmentAvailableDateResponseDTO parseAvailableAppointmentDates(List<LocalDate> availableDates) {
        AppointmentAvailableDateResponseDTO responseDTO = new AppointmentAvailableDateResponseDTO();
        responseDTO.setAvailableDates(availableDates);
        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);

        return responseDTO;
    }

}
