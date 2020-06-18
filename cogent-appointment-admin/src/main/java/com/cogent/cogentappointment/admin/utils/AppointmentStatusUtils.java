package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.*;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/12/2019
 */
public class AppointmentStatusUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    /*ADD TO LIST ONLY IF DoctorTimeSlotResponseDTO LIST DOES NOT CONTAIN DOCTOR DUTY ROSTER TIME*/
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAllAppointmentStatus(
            java.time.LocalDate availableDate,
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
                setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                doctorTimeSlots.add(doctorTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        sortByAppointmentTime(doctorTimeSlots);

        return doctorTimeSlots;
    }

    /*SORT BY APPOINTMENT TIME*/
    private static void sortByAppointmentTime(List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {
        doctorTimeSlots.sort((o1, o2) -> {

            try {
                return new SimpleDateFormat("hh:mm a").parse(o1.getAppointmentTime())
                        .compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getAppointmentTime()));
            } catch (ParseException e) {
                return 0;
            }
        });
    }

    private static void sortByDepartmentAppointmentTime(List<AppointmentTimeSlotResponseDTO> doctorTimeSlots) {
        doctorTimeSlots.sort((o1, o2) -> {

            try {
                return new SimpleDateFormat("hh:mm a").parse(o1.getAppointmentTime())
                        .compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getAppointmentTime()));
            } catch (ParseException e) {
                return 0;
            }
        });
    }

    /*IF STATUS IN SEARCH IS 'V'(VACANT), THEN ONLY ADD VACANT TIME SLOTS.
     * ADD ONLY THOSE TIME WHICH DOES NOT MATCH WITH APPOINTMENT - NO NEED TO SHOW APPOINTMENT WITH OTHER STATUS
     * */
    public static List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForVacantAppointmentStatus(
            java.time.LocalDate availableDate,
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
                    setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                    doctorTimeSlots.add(doctorTimeSlotResponseDTO);
                }

            } else {
                setTimeSlotMapWithAppointmentStatus(doctorTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                doctorTimeSlots.add(doctorTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return doctorTimeSlots;
    }

    private static void setTimeSlotMapWithAppointmentStatus(DoctorTimeSlotResponseDTO responseDTO,
                                                            DateTime dateTime,
                                                            java.time.LocalDate availableDate,
                                                            String status) {

        responseDTO.setAppointmentTime(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
        responseDTO.setHasTimePassed(hasTimePassed(availableDate, FORMAT.print(dateTime)));
        responseDTO.setStatus(status);
    }

    private static void setTimeSlotMapWithDepartmentAppointmentStatus(AppointmentTimeSlotResponseDTO responseDTO,
                                                                      DateTime dateTime,
                                                                      java.time.LocalDate availableDate,
                                                                      String status) {

        responseDTO.setAppointmentTime(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
        responseDTO.setHasTimePassed(hasTimePassed(availableDate, FORMAT.print(dateTime)));
        responseDTO.setStatus(status);
    }

    private static Date parseAppointmentTime(Date availableDate, String availableTime) {
        return datePlusTime(utilDateToSqlDate(availableDate), Objects.requireNonNull(parseTime(availableTime)));
    }

    public static void parseAppointmentDetails
            (AppointmentStatusResponseDTO appointmentStatusResponseDTO,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlotResponseDTOS) {

        DoctorTimeSlotResponseDTO responseDTO = new DoctorTimeSlotResponseDTO();

    /*APPOINTMENT TIME - APPOINTMENT STATUS*/
        String[] appointmentTimeDetails = appointmentStatusResponseDTO.getAppointmentTimeDetails()
                .split(COMMA_SEPARATED);

        for (String appointmentTimeAndStatus : appointmentTimeDetails) {
            String[] timeAndStatus = appointmentTimeAndStatus.split(HYPHEN);

            String appointmentTime = timeAndStatus[0];

            responseDTO.setAppointmentTime(convert24HourTo12HourFormat(appointmentTime));
            responseDTO.setStatus(timeAndStatus[1]);
            responseDTO.setAppointmentNumber(appointmentStatusResponseDTO.getAppointmentNumber());
            responseDTO.setPatientName(appointmentStatusResponseDTO.getPatientName());
            responseDTO.setAge(appointmentStatusResponseDTO.getAge());
            responseDTO.setMobileNumber(appointmentStatusResponseDTO.getMobileNumber());
            responseDTO.setGender(appointmentStatusResponseDTO.getGender());
            responseDTO.setAppointmentId(appointmentStatusResponseDTO.getAppointmentId());
            responseDTO.setHasTimePassed(hasTimePassed(appointmentStatusResponseDTO.getDate(), appointmentTime));
            responseDTO.setIsFollowUp(appointmentStatusResponseDTO.getIsFollowUp());
            responseDTO.setHasTransferred(appointmentStatusResponseDTO.getHasTransferred());
        }

        doctorTimeSlotResponseDTOS.add(responseDTO);
    }

    private static boolean hasTimePassed(java.time.LocalDate date,
                                         String time) {

        Date availableDateTime = parseAppointmentTime(convertLocalDateToDate(date), time);

        Date currentDate = new Date();

        return availableDateTime.before(currentDate);
    }

    private static boolean hasTimePassed(Date date, String time) {

        Date availableDateTime = parseAppointmentTime(date, time);

        Date currentDate = new Date();

        return availableDateTime.before(currentDate);
    }

    public static AppointmentStatusDTO parseToAppointmentStatusDTO(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRostersInfo,
            List<DoctorDropdownDTO> doctorInfo) {

        return AppointmentStatusDTO.builder()
                .doctorDutyRosterInfo(doctorDutyRostersInfo)
                .doctorInfo(doctorInfo)
                .build();
    }

    public static HospitalDeptAppointmentStatusDTO parseToHospitalDeptAppointmentStatusDTO(
            List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRostersInfo,
            List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorDTOS) {

        return HospitalDeptAppointmentStatusDTO.builder()
                .hospitalDeptDutyRosterInfo(hospitalDeptDutyRostersInfo)
                .hospitalDeptAndDoctorInfo(hospitalDeptAndDoctorDTOS)
                .build();
    }

    public static List<AppointmentTimeSlotResponseDTO> parseToAppointmentTimeSlotResponseDTOS(
            AppointmentDetailsForStatus appointmentDetailsForStatus) {

        List<AppointmentTimeSlotResponseDTO> responseDTOS= new ArrayList<> ();

        AppointmentTimeSlotResponseDTO response=AppointmentTimeSlotResponseDTO.builder()
                        .appointmentTime(appointmentDetailsForStatus.getAppointmentTime())
                        .status(appointmentDetailsForStatus.getStatus())
                        .appointmentNumber(appointmentDetailsForStatus.getAppointmentNumber())
                        .mobileNumber(appointmentDetailsForStatus.getMobileNumber())
                        .age(appointmentDetailsForStatus.getAge())
                        .gender(String.valueOf(appointmentDetailsForStatus.getGender()))
                        .patientName(appointmentDetailsForStatus.getPatientName())
                        .appointmentId(appointmentDetailsForStatus.getAppointmentId())
                        .hasTransferred(appointmentDetailsForStatus.getHasTransferred())
                        .isFollowUp(appointmentDetailsForStatus.getIsFollowUp())
                        .hasTimePassed(hasTimePassed(appointmentDetailsForStatus.getAppointmentDate(),
                                appointmentDetailsForStatus.getAppointmentTime()))
                        .build();
        responseDTOS.add(response);

        return responseDTOS;
    }

    public static List<HospitalDeptDutyRosterStatusResponseDTO> parseHospitalDeptDutyRosterStatusResponseDTOS
            (List<AppointmentTimeSlotResponseDTO> appointmentTimeSlotResponseDTOS,
             RosterDetailsForStatus rosterDetailsForStatus,
             AppointmentDetailsForStatus appointmentDetailsForStatus) {

        List<HospitalDeptDutyRosterStatusResponseDTO> responseDTOS = new ArrayList<>();

        HospitalDeptDutyRosterStatusResponseDTO responseDTO = new HospitalDeptDutyRosterStatusResponseDTO();

        responseDTO.setAppointmentTimeSlots(appointmentTimeSlotResponseDTOS);
        responseDTO.setHospitalDepartmentDutyRosterId(rosterDetailsForStatus.getRosterId());
        responseDTO.setUniqueIdentifier(appointmentDetailsForStatus.getHospitalDepartmentId()
                + "-" + appointmentDetailsForStatus.getAppointmentDate());
        responseDTO.setDate(convertDateToLocalDate(appointmentDetailsForStatus.getAppointmentDate()));
        responseDTO.setStartTime(rosterDetailsForStatus.getStartTime());
        responseDTO.setEndTime(rosterDetailsForStatus.getEndTime());
        responseDTO.setDayOffStatus(rosterDetailsForStatus.getDayOffStatus());
        responseDTO.setRosterGapDuration(rosterDetailsForStatus.getRosterGapDuration());
        responseDTO.setHospitalDepartmentId(appointmentDetailsForStatus.getHospitalDepartmentId());
        responseDTO.setHospitalDepartmentName(appointmentDetailsForStatus.getHospitalDepartmentName());
        responseDTO.setHospitalDepartmentRoomInfoId(appointmentDetailsForStatus.getHospitalDepartmentRoomInfoId());
        responseDTO.setRoomNumber(appointmentDetailsForStatus.getRoomNumber());
        responseDTO.setWeekDayName(convertDateToLocalDate(
                appointmentDetailsForStatus.getAppointmentDate()).getDayOfWeek().toString());
        responseDTO.setRoomList(null);

        responseDTOS.add(responseDTO);

        return responseDTOS;
    }


    public static void parseDepartmentAppointmentDetails
            (HospitalDeptAppointmentStatusResponseDTO appointmentStatusResponseDTO,
             List<AppointmentTimeSlotResponseDTO> appointmentTimeSlotResponseDTOS) {

        AppointmentTimeSlotResponseDTO responseDTO = new AppointmentTimeSlotResponseDTO();

    /*APPOINTMENT TIME - APPOINTMENT STATUS*/
        String[] appointmentTimeDetails = appointmentStatusResponseDTO.getAppointmentTimeDetails()
                .split(COMMA_SEPARATED);

        for (String appointmentTimeAndStatus : appointmentTimeDetails) {
            String[] timeAndStatus = appointmentTimeAndStatus.split(HYPHEN);

            String appointmentTime = timeAndStatus[0];

            responseDTO.setAppointmentTime(convert24HourTo12HourFormat(appointmentTime));
            responseDTO.setStatus(timeAndStatus[1]);
            responseDTO.setAppointmentNumber(appointmentStatusResponseDTO.getAppointmentNumber());
            responseDTO.setPatientName(appointmentStatusResponseDTO.getPatientName());
            responseDTO.setAge(appointmentStatusResponseDTO.getAge());
            responseDTO.setMobileNumber(appointmentStatusResponseDTO.getMobileNumber());
            responseDTO.setGender(appointmentStatusResponseDTO.getGender());
            responseDTO.setAppointmentId(appointmentStatusResponseDTO.getAppointmentId());
            responseDTO.setHasTimePassed(hasTimePassed(appointmentStatusResponseDTO.getDate(), appointmentTime));
            responseDTO.setIsFollowUp(appointmentStatusResponseDTO.getIsFollowUp());
            responseDTO.setHasTransferred(appointmentStatusResponseDTO.getHasTransferred());
        }

        appointmentTimeSlotResponseDTOS.add(responseDTO);
    }

    public static List<AppointmentTimeSlotResponseDTO> calculateTimeSlotsForAllDepartmentAppointmentStatus(
            java.time.LocalDate availableDate,
            String startTime,
            String endTime,
            int durationInMinutes,
            List<AppointmentTimeSlotResponseDTO> doctorTimeSlots) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            AppointmentTimeSlotResponseDTO appointmentTimeSlotResponseDTO = new AppointmentTimeSlotResponseDTO();

            DateTime finalDateTime = dateTime;

            boolean timeMatched = doctorTimeSlots.stream()
                    .anyMatch(doctorTimeSlot -> Objects.equals(doctorTimeSlot.getAppointmentTime(),
                            convert24HourTo12HourFormat(FORMAT.print(finalDateTime)))
                    );

            if (!timeMatched) {
                setTimeSlotMapWithDepartmentAppointmentStatus(appointmentTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                doctorTimeSlots.add(appointmentTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        sortByDepartmentAppointmentTime(doctorTimeSlots);

        return doctorTimeSlots;
    }


    public static List<AppointmentTimeSlotResponseDTO> calculateTimeSlotsForVacantDepartmentAppointmentStatus(
            java.time.LocalDate availableDate,
            String startTime,
            String endTime,
            int durationInMinutes,
            String matchedAppointmentWithStatus,
            List<AppointmentTimeSlotResponseDTO> appointmentTimeSlots) {

        final Duration duration = Minutes.minutes(durationInMinutes).toStandardDuration();

        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            AppointmentTimeSlotResponseDTO appointmentTimeSlotResponseDTO = new AppointmentTimeSlotResponseDTO();

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
                    setTimeSlotMapWithDepartmentAppointmentStatus(appointmentTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                    appointmentTimeSlots.add(appointmentTimeSlotResponseDTO);
                }

            } else {
                setTimeSlotMapWithDepartmentAppointmentStatus(appointmentTimeSlotResponseDTO, dateTime, availableDate, VACANT);
                appointmentTimeSlots.add(appointmentTimeSlotResponseDTO);
            }

            dateTime = dateTime.plus(duration);

        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return appointmentTimeSlots;
    }
}
