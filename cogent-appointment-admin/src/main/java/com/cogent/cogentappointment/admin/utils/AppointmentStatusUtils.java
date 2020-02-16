package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAllAppointmentStatus(
            String startTime,
            String endTime,
            int durationInMinutes,
            AppointmentStatusResponseDTO appointmentStatus,
            String searchAppointmentStatus,
            List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO = new DoctorTimeSlotResponseDTO();

            if (!Objects.isNull(appointmentStatus)) {

                /*APPOINTMENT TIME - APPOINTMENT STATUS*/
                String[] appointmentTime = appointmentStatus.getAppointmentTimeDetails().split(COMMA_SEPARATED);

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
                            doctorTimeSlots);

                else setTimeSlotMapForOtherAppointmentStatus(doctorTimeSlotResponseDTO, timeMatched, dateTime,
                        doctorTimeSlots, appointmentStatus);

            } else {
                setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                doctorTimeSlots.add(doctorTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return doctorTimeSlots;
    }

    /*IF STATUS IN SEARCH IS OTHER THAN 'V'(VACANT), THEN SHOW APPOINTMENT AS PER STATUS*/
    private static void setTimeSlotMapForOtherAppointmentStatus(DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO,
                                                                String timeMatched,
                                                                DateTime dateTime,
                                                                List<DoctorTimeSlotResponseDTO> doctorTimeSlots,
                                                                AppointmentStatusResponseDTO appointmentStatus) {

        if (!Objects.isNull(timeMatched)) {
            String[] timeMatchedSplit = timeMatched.split(HYPHEN);
            setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, timeMatchedSplit[1]);

            parseAppointmentDetails(doctorTimeSlotResponseDTO, appointmentStatus);
        } else
            setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);

        doctorTimeSlots.add(doctorTimeSlotResponseDTO);
    }

    /*IF STATUS IN SEARCH IS 'V'(VACANT), THEN ONLY ADD VACANT TIME SLOTS.
     * ADD ONLY THOSE TIME WHICH MATCHES WITH APPOINTMENT - NO NEED TO SHOW APPOINTMENT WITH OTHER STATUS
     * */
    private static void setTimeSlotForVacantAppointmentStatus(DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO,
                                                              String timeMatched,
                                                              DateTime dateTime,
                                                              List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {
        if (Objects.isNull(timeMatched)) {
            setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
            doctorTimeSlots.add(doctorTimeSlotResponseDTO);
        }
    }

    private static void setTimeSlotMapWithAppointmentStatus(DoctorTimeSlotResponseDTO responseDTO,
                                                            DateTime dateTime,
                                                            String status) {

        responseDTO.setAppointmentTime(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
        responseDTO.setStatus(status);
    }

    public static void parseAppointmentDetails(DoctorTimeSlotResponseDTO responseDTO,
                                               AppointmentStatusResponseDTO appointmentStatusResponseDTO) {

        responseDTO.setAppointmentNumber(appointmentStatusResponseDTO.getAppointmentNumber());
        responseDTO.setPatientName(appointmentStatusResponseDTO.getPatientName());
        responseDTO.setAge(appointmentStatusResponseDTO.getAge());
        responseDTO.setMobileNumber(appointmentStatusResponseDTO.getMobileNumber());
        responseDTO.setGender(appointmentStatusResponseDTO.getGender());
    }

    public static AppointmentStatusDTO parseToAppointmentStatusDTO(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRostersInfo,
            List<DoctorDropdownDTO> doctorInfo) {

        return AppointmentStatusDTO.builder()
                .doctorDutyRosterInfo(doctorDutyRostersInfo)
                .doctorInfo(doctorInfo)
                .build();
    }

}
