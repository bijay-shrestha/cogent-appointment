package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
public class AppointmentHospitalDeptUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithRoom(
            Date queryDate,
            List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomList,
            HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo,
            String startTime,
            String endTime,
            List<String> availableTimeSlots) {

        AppointmentHospitalDeptCheckAvailabilityResponseDTO responseDTO =
                AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                        .queryDate(queryDate)
                        .hasRoom(YES)
                        .roomInfo(availableRoomList)
                        .hospitalDepartmentRoomInfoId(roomInfo.getHospitalDepartmentRoomInfoId())
                        .roomNumber(roomInfo.getRoomNumber())
                        .hospitalDepartmentAvailableTime(startTime + HYPHEN + endTime)
                        .availableTimeSlots(availableTimeSlots)
                        .build();

        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
        return responseDTO;
    }

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithoutRoom(
            Date queryDate,
            String startTime,
            String endTime,
            List<String> availableTimeSlots) {

        AppointmentHospitalDeptCheckAvailabilityResponseDTO responseDTO =
                AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                        .queryDate(queryDate)
                        .hasRoom(NO)
                        .roomInfo(new ArrayList<>())
                        .hospitalDepartmentAvailableTime(startTime + HYPHEN + endTime)
                        .availableTimeSlots(availableTimeSlots)
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

            if ((!isAppointmentDateMatched(bookedAppointments, date)))
//                    && (!hasTimePassed(requestedDate, date)))
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
}
