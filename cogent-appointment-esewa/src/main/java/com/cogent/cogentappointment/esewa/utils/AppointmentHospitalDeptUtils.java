package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;

/**
 * @author smriti on 29/05/20
 */
public class AppointmentHospitalDeptUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithRoom(
            Date queryDate,
            List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomList,
            HospitalDeptDutyRosterRoomInfoResponseDTO roomInfo
    ) {

        return AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                .queryDate(queryDate)
                .hasRoom(YES)
                .roomInfo(availableRoomList)
                .roomId(roomInfo.getRoomId())
                .roomNumber(roomInfo.getRoomNumber())
                .build();
    }

    public static AppointmentHospitalDeptCheckAvailabilityResponseDTO parseToAvailabilityResponseWithoutRoom(
            Date queryDate,
            List<HospitalDeptDutyRosterRoomInfoResponseDTO> availableRoomList) {

        return AppointmentHospitalDeptCheckAvailabilityResponseDTO.builder()
                .queryDate(queryDate)
                .hasRoom(NO)
                .roomInfo(availableRoomList)
                .build();
    }


    public static List<String> calculateAvailableTimeSlots(String startTime,
                                                           String endTime,
                                                           Duration rosterGapDuration,
                                                           List<AppointmentBookedTimeResponseDTO> bookedAppointments) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if (!isAppointmentDateMatched(bookedAppointments, date))
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
}
