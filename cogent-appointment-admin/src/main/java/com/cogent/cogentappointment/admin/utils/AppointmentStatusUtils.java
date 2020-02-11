package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convert24HourTo12HourFormat;

/**
 * @author smriti ON 16/12/2019
 */
public class AppointmentStatusUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAllAppointmentStatus(String startTime,
                                                                                            String endTime,
                                                                                            int durationInMinutes,
                                                                                            String appointmentTimeDetails,
                                                                                            String searchAppointmentStatus) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        List<DoctorTimeSlotResponseDTO> timeSlotResponseDTOS = new ArrayList<>();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO = new DoctorTimeSlotResponseDTO();

            if (!Objects.isNull(appointmentTimeDetails)) {

                /*APPOINTMENT TIME - APPOINTMENT STATUS*/
                String[] appointmentTime = appointmentTimeDetails.split(COMMA_SEPARATED);

                DateTime finalDateTime = dateTime;
                String timeMatched = Arrays.stream(appointmentTime)
                        .filter(str -> {
                            String date = FORMAT.print(finalDateTime);
                            return str.contains(date);
                        })
                        .findAny()
                        .orElse(null);

                if (searchAppointmentStatus.equals(VACANT))
                    setTimeSlotForVacantAppointmentStatus(doctorTimeSlotResponseDTO, timeMatched, dateTime,
                            timeSlotResponseDTOS);

                else setTimeSlotMapForOtherAppointmentStatus(doctorTimeSlotResponseDTO, timeMatched, dateTime,
                        timeSlotResponseDTOS);

            } else {
                setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                timeSlotResponseDTOS.add(doctorTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return timeSlotResponseDTOS;
    }

    /*IF STATUS IN SEARCH IS OTHER THAN 'V'(VACANT), THEN SHOW APPOINTMENT AS PER STATUS*/
    private static void setTimeSlotMapForOtherAppointmentStatus(DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO,
                                                                String timeMatched,
                                                                DateTime dateTime,
                                                                List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        if (!Objects.isNull(timeMatched)) {
            String[] timeMatchedSplit = timeMatched.split(HYPHEN);
            setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, timeMatchedSplit[1]);
        } else
            setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);

        doctorTimeSlots.add(doctorTimeSlotResponseDTO);
    }

    /*IF STATUS IN SEARCH IS 'V'(VACANT), THEN ONLY ADD VACANT TIMESLOTS.
     * NO NEED TO SHOW APPOINTMENT WITH OTHER STATUS*/
    private static void setTimeSlotForVacantAppointmentStatus(DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO,
                                                              String timeMatched,
                                                              DateTime dateTime,
                                                              List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {
        if (Objects.isNull(timeMatched)) {
            setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
            doctorTimeSlots.add(doctorTimeSlotResponseDTO);
        }
    }

    private static void setTimeSlotMapForAllAppointmentStatus(DoctorTimeSlotResponseDTO responseDTO,
                                                              DateTime dateTime,
                                                              String status) {

        responseDTO.setAppointmentTime(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
        responseDTO.setStatus(status);
    }

    /*IF STATUS IN SEARCH DTO = 'V' (VACANT), THEN RETURN DOCTOR DUTY ROSTER TIME RANGE
   NO NEED TO FILTER WITH APPOINTMENT LIST*/
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForVacantAppointmentStatus(String startTime,
                                                                                               String endTime,
                                                                                               int durationInMinutes,
                                                                                               String appointmentTimeDetails) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        List<DoctorTimeSlotResponseDTO> timeSlotResponseDTOS = new ArrayList<>();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO = new DoctorTimeSlotResponseDTO();

            if (!Objects.isNull(appointmentTimeDetails)) {

                /*APPOINTMENT TIME - APPOINTMENT STATUS*/
                String[] appointmentTime = appointmentTimeDetails.split(COMMA_SEPARATED);

                DateTime finalDateTime = dateTime;
                String timeMatched = Arrays.stream(appointmentTime)
                        .filter(str -> {
                            String date = FORMAT.print(finalDateTime);
                            return str.contains(date);
                        })
                        .findAny()
                        .orElse(null);

                if (!Objects.isNull(timeMatched)) {
                    String[] timeMatchedSplit = timeMatched.split(HYPHEN);

                    setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, timeMatchedSplit[1]);
                } else {
                    setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                }
            } else {
                setTimeSlotMapForAllAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
            }

            dateTime = dateTime.plus(duration);

            timeSlotResponseDTOS.add(doctorTimeSlotResponseDTO);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return timeSlotResponseDTOS;
    }

    /*IF STATUS IN SEARCH DTO IS NOT EMPTY, THEN RETURN ONLY APPOINTMENT DETAILS WITH RESPECTIVE STATUS.
     NO NEED TO FILTER WITH DOCTOR DUTY ROSTER RANGE*/
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForSelectedAppointmentStatus(
            List<AppointmentStatusResponseDTO> appointments) {

        List<DoctorTimeSlotResponseDTO> doctorTimeSlotResponseDTOS = new ArrayList<>();

        appointments.forEach(appointment -> {

            DoctorTimeSlotResponseDTO responseDTO = new DoctorTimeSlotResponseDTO();

            /*APPOINTMENT TIME - APPOINTMENT STATUS*/
            String[] appointmentTimeDetails = appointment.getAppointmentTimeDetails().split(COMMA_SEPARATED);

            for (String appointmentTimeAndStatus : appointmentTimeDetails) {
                String[] timeAndStatus = appointmentTimeAndStatus.split(HYPHEN);

                responseDTO.setAppointmentTime(timeAndStatus[0]);
                responseDTO.setStatus(timeAndStatus[1]);

                doctorTimeSlotResponseDTOS.add(responseDTO);
            }
        });

        return doctorTimeSlotResponseDTOS;
    }

}
