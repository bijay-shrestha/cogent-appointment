package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.model.Appointment;
import com.cogent.cogentappointment.client.model.Doctor;
import com.cogent.cogentappointment.client.model.Patient;
import com.cogent.cogentappointment.client.model.Specialization;
import com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static Appointment parseToAppointment(AppointmentRequestDTO requestDTO,
                                                 String appointmentNumber,
                                                 Patient patient,
                                                 Specialization specialization,
                                                 Doctor doctor) {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setAppointmentTime(parseAppointmentTime(requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime()));
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setSerialNumber(NumberFormatterUtils.generateRandomNumber(6));
        appointment.setCreatedDateNepali(requestDTO.getCreatedDateNepali());
        appointment.setStatus("PA");
//        appointment.setHospitalId();

        parseToAppointment(patient, specialization, doctor, appointment);
        return appointment;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

    private static void parseToAppointment(Patient patient,
                                           Specialization specialization,
                                           Doctor doctor,
                                           Appointment appointment) {
        appointment.setDoctorId(doctor);
        appointment.setSpecializationId(specialization);
        appointment.setPatientId(patient);
    }

    public static void parseToUpdatedAppointment(Appointment appointment,
                                                 AppointmentUpdateRequestDTO updateRequestDTO,
                                                 Patient patient) {

        appointment.setAppointmentDate(updateRequestDTO.getAppointmentDate());
//        appointment.setStartTime(updateRequestDTO.getStartTime());
//        appointment.setEndTime(updateRequestDTO.getEndTime());
//        appointment.setStatus(updateRequestDTO.getStatus());

        appointment.setRemarks(updateRequestDTO.getRemarks());
//        parseToAppointment(appointment, patient);
    }


    public static void convertToCancelledAppointment(Appointment appointment,
                                                     AppointmentCancelRequestDTO cancelRequestDTO) {
        appointment.setRemarks(cancelRequestDTO.getRemarks());
//        appointment.setStatus(cancelRequestDTO.getStatus());
    }

    public static String generateAppointmentNumber(List results) {
        return results.isEmpty() ? "0001" :
                String.format("%04d", Integer.parseInt(results.get(0).toString()) + 1);
    }

    public static void parseToRescheduleAppointment(Appointment appointment,
                                                    AppointmentRescheduleRequestDTO rescheduleRequestDTO) {
        appointment.setAppointmentDate(rescheduleRequestDTO.getAppointmentDate());
//        appointment.setStartTime(rescheduleRequestDTO.getStartTime());
//        appointment.setEndTime(rescheduleRequestDTO.getEndTime());
        appointment.setRemarks(rescheduleRequestDTO.getRemarks());
    }


//    public static AppointmentAvailabilityResponseDTO parseToAppointmentAvailabilityResponseDTO
//            (List<AppointmentTimeResponseDTO> appointmentTimeResponseDTOS,
//             DoctorDutyRosterTimeResponseDTO doctorDutyRosterTimeResponseDTO) {
//
//        return AppointmentAvailabilityResponseDTO.builder()
//                .appointmentTimeResponseDTOS(appointmentTimeResponseDTOS)
//                .doctorDutyRosterTimeResponseDTO(doctorDutyRosterTimeResponseDTO)
//                .build();
//    }

//    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentStatusResponseDTOS
//            (List<Object[]> results) {
//
//        List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS = new ArrayList<>();
//
//        results.forEach(result -> {
//            final int APPOINTMENT_DATE_INDEX = 0;
//            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
//            final int DOCTOR_ID_INDEX = 2;
//            final int DOCTOR_NAME_INDEX = 3;
//            final int SPECIALIZATION_ID_INDEX = 4;
//            final int SPECIALIZATION_NAME_INDEX = 5;
//
//            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
//
//            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();
//
//            AppointmentStatusResponseDTO appointmentStatusResponseDTO = AppointmentStatusResponseDTO.builder()
//                    .date(appointmentLocalDate)
//                    .startTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
//                    .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
//                    .doctorName(result[DOCTOR_NAME_INDEX].toString())
//                    .specializationId(Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
//                    .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
//                    .build();
//
//            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
//        });
//
//        return appointmentStatusResponseDTOS;
//    }

    public static AppointmentCheckAvailabilityResponseDTO parseToAppointmentResponseWithEndTime
            (DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
             List<AppointmentAvailabilityResponseDTO> availableSlots) {

        return AppointmentCheckAvailabilityResponseDTO.builder()
                .doctorStartTime(getTimeIn12HourFormat(doctorDutyRosterInfo.getStartTime()))
                .doctorEndTime(getTimeIn12HourFormat(doctorDutyRosterInfo.getEndTime()))
                .dayOffStatus(doctorDutyRosterInfo.getDayOffStatus())
                .availableAppointments(availableSlots)
                .build();
    }

    public static List<AppointmentAvailabilityResponseDTO> parseToResponseDTOWithEndTime(
            DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
            List<AppointmentBookedTimeResponseDTO> bookedAppointments) {

        List<AppointmentAvailabilityResponseDTO> availableTimeSlots = new ArrayList<>();

        final Duration duration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration()).toStandardDuration();

        String startTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String endTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            AppointmentAvailabilityResponseDTO responseDTO = new AppointmentAvailabilityResponseDTO();

            String date = FORMAT.print(startDateTime);

            if (!isAppointmentDateMatched(bookedAppointments, date)) {
                setTimeSlotMap(responseDTO, startDateTime, duration);
                availableTimeSlots.add(responseDTO);
            }

            startDateTime = startDateTime.plus(duration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
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

    public static AppointmentCheckAvailabilityMinResponseDTO parseToAvailabilityResponse(
            String startTime,
            String endTime,
            Date queryDate,
            List<String> availableTimeSlots) {

        return AppointmentCheckAvailabilityMinResponseDTO.builder()
                .queryDate(queryDate)
                .doctorAvailableTime(startTime + HYPHEN + endTime)
                .availableTimeSlots(availableTimeSlots)
                .build();
    }

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getAppointmentTime().contains(date));
    }

    private static void setTimeSlotMap(AppointmentAvailabilityResponseDTO responseDTO,
                                       DateTime startDateTime,
                                       Duration durationInMinutes) {
        responseDTO.setStartTime(getTimeIn12HourFormat(startDateTime.toDate()));
        responseDTO.setEndTime(getTimeIn12HourFormat(startDateTime.plus(durationInMinutes).toDate()));
    }

}
