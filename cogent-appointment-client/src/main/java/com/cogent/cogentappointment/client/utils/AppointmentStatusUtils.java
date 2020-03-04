package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convert24HourTo12HourFormat;

/**
 * @author smriti ON 16/12/2019
 */
public class AppointmentStatusUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    /*ADD TO LIST ONLY IF DoctorTimeSlotResponseDTO LIST DOES NOT CONTAIN DOCTOR DUTY ROSTER TIME*/
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAllAppointmentStatus(
            String startTime,
            String endTime,
            int durationInMinutes,
            List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO = new DoctorTimeSlotResponseDTO();

            DateTime finalDateTime = dateTime;

            boolean timeMatched = doctorTimeSlots.stream()
                    .anyMatch(doctorTimeSlot -> Objects.equals(doctorTimeSlot.getAppointmentTime(),
                            convert24HourTo12HourFormat(FORMAT.print(finalDateTime)))
                    );

            if (!timeMatched) {
                setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                doctorTimeSlots.add(doctorTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        /*SORT BY APPOINTMENT TIME*/
        doctorTimeSlots.sort((o1, o2) -> {

            try {
                return new SimpleDateFormat("hh:mm a").parse(o1.getAppointmentTime())
                        .compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getAppointmentTime()));
            } catch (ParseException e) {
                return 0;
            }
        });

        return doctorTimeSlots;
    }

    /*IF STATUS IN SEARCH IS 'V'(VACANT), THEN ONLY ADD VACANT TIME SLOTS.
     * ADD ONLY THOSE TIME WHICH DOES NOT MATCH WITH APPOINTMENT - NO NEED TO SHOW APPOINTMENT WITH OTHER STATUS
     * */
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForVacantAppointmentStatus(
            String startTime,
            String endTime,
            int durationInMinutes,
            String matchedAppointmentWithStatus,
            List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            DoctorTimeSlotResponseDTO doctorTimeSlotResponseDTO = new DoctorTimeSlotResponseDTO();

            if (!Objects.isNull(matchedAppointmentWithStatus)) {
                 /*APPOINTMENT TIME - APPOINTMENT STATUS*/
                String[] appointmentTime = matchedAppointmentWithStatus.split(COMMA_SEPARATED);

                DateTime finalDateTime = dateTime;

                String timeMatched = Arrays.stream(appointmentTime)
                        .filter(str -> {
                            String date = FORMAT.print(finalDateTime);
                            return str.contains(date);
                        })
                        .findAny()
                        .orElse(null);

                if (Objects.isNull(timeMatched)) {
                    setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                    doctorTimeSlots.add(doctorTimeSlotResponseDTO);
                }

            } else {
                setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, VACANT);
                doctorTimeSlots.add(doctorTimeSlotResponseDTO);
            }


            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return doctorTimeSlots;
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
        responseDTO.setAppointmentId(appointmentStatusResponseDTO.getAppointmentId());
    }

    public static AppointmentStatusDTO parseToAppointmentStatusDTO(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRostersInfo,
            List<DoctorDropdownDTO> doctorInfo) {

        return AppointmentStatusDTO.builder()
                .doctorDutyRosterInfo(doctorDutyRostersInfo)
                .doctorInfo(doctorInfo)
                .build();
    }

    public static AppointmentStatusRequestDTO parseToAppointmentStatusRequestDTO(
            Date toDate, Date fromDate, Long specializationId,
            Long doctorId, String status) {
       return AppointmentStatusRequestDTO.builder()
                .toDate(toDate)
                .fromDate(fromDate)
                .doctorId(doctorId)
                .specializationId(specializationId)
                .status(status)
                .build();

    }


}
