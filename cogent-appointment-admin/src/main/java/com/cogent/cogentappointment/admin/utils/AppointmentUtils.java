package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.*;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
        appointment.setStartTime(requestDTO.getStartTime());
        appointment.setEndTime(requestDTO.getEndTime());
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setUniqueId(NumberFormatterUtils.generateRandomNumber(6));
        appointment.setCreatedDateNepali(requestDTO.getCreatedDateNepali());
        appointment.setStatus(requestDTO.getStatus());

        parseToAppointment(patient, specialization, doctor, appointment);
        return appointment;
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
        appointment.setStartTime(updateRequestDTO.getStartTime());
        appointment.setEndTime(updateRequestDTO.getEndTime());
        appointment.setStatus(updateRequestDTO.getStatus());

        appointment.setRemarks(updateRequestDTO.getRemarks());
//        parseToAppointment(appointment, patient);
    }


    public static void convertToCancelledAppointment(Appointment appointment,
                                                     AppointmentCancelRequestDTO cancelRequestDTO) {
        appointment.setRemarks(cancelRequestDTO.getRemarks());
        appointment.setStatus(cancelRequestDTO.getStatus());
    }

    public static String generateAppointmentNumber(List results) {
        return results.isEmpty() ? "0001" :
                String.format("%04d", Integer.parseInt(results.get(0).toString()) + 1);
    }

    public static void parseToRescheduleAppointment(Appointment appointment,
                                                    AppointmentRescheduleRequestDTO rescheduleRequestDTO) {
        appointment.setAppointmentDate(rescheduleRequestDTO.getAppointmentDate());
        appointment.setStartTime(rescheduleRequestDTO.getStartTime());
        appointment.setEndTime(rescheduleRequestDTO.getEndTime());
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

//    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentApprovalResponse
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

    public static AppointmentCheckAvailabilityResponseDTO parseToAppointmentCheckAvailabilityResponseDTO
            (DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
             List<AppointmentAvailabilityResponseDTO> availableSlots) {

        return AppointmentCheckAvailabilityResponseDTO.builder()
                .doctorStartTime(DateUtils.getTimeIn12HourFormat(doctorDutyRosterInfo.getStartTime()))
                .doctorEndTime(DateUtils.getTimeIn12HourFormat(doctorDutyRosterInfo.getEndTime()))
                .dayOffStatus(doctorDutyRosterInfo.getDayOffStatus())
                .availableAppointments(availableSlots)
                .build();
    }

    public static List<AppointmentAvailabilityResponseDTO> parseToAppointmentAvailabilityResponseDTO(
            DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
            List<AppointmentBookedTimeResponseDTO> bookedAppointments) {

        List<AppointmentAvailabilityResponseDTO> availableTimeSlots = new ArrayList<>();

        final Duration duration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration()).toStandardDuration();

        String startTime = DateUtils.getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String endTime = DateUtils.getTimeFromDate(doctorDutyRosterInfo.getEndTime());

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

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getStartTime().contains(date));
    }

    private static void setTimeSlotMap(AppointmentAvailabilityResponseDTO responseDTO,
                                       DateTime startDateTime,
                                       Duration durationInMinutes) {
        responseDTO.setStartTime(DateUtils.getTimeIn12HourFormat(startDateTime.toDate()));
        responseDTO.setEndTime(DateUtils.getTimeIn12HourFormat(startDateTime.plus(durationInMinutes).toDate()));
    }

    public static AppointmentPendingApprovalResponseDTO parseQueryResultToAppointmentApprovalResponse
            (List<Object[]> results) {

        AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO = new AppointmentPendingApprovalResponseDTO();

        List<AppointmentPendingApprovalDTO> appointmentPendingApprovalDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int APPOINTMENT_DATE_INDEX = 1;
            final int APPOINTMENT_NUMBER_INDEX = 2;
            final int APPOINTMENT_TIME_INDEX = 3;
            final int ESEWA_ID_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_GENDER_INDEX = 7;
            final int PATIENT_DOB_INDEX = 8;
            final int IS_REGISTERED_INDEX = 9;
            final int IS_SELF_INDEX = 10;
            final int PATIENT_MOBILE_NUMBER_INDEX = 11;
            final int SPECIALIZATION_NAME_INDEX = 12;
            final int TRANSACTION_NUMBER_INDEX = 13;
            final int APPOINTMENT_AMOUNT_INDEX = 14;
            final int DOCTOR_NAME_INDEX = 15;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            AppointmentPendingApprovalDTO appointmentStatusResponseDTO =
                    AppointmentPendingApprovalDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber((result[REGISTRATION_NUMBER_INDEX] == null)
                                    ? null : result[REGISTRATION_NUMBER_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(AgeConverterUtils.calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .build();

            appointmentPendingApprovalDTOS.add(appointmentStatusResponseDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentPendingApprovalResponseDTO.setPendingAppointmentApprovals(appointmentPendingApprovalDTOS);
        appointmentPendingApprovalResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentPendingApprovalResponseDTO;
    }

    public static AppointmentLogSearchResponseDTO parseQueryResultToAppointmentLogResponse(List<Object[]> results) {

        AppointmentLogSearchResponseDTO appointmentLogSearchResponseDTO = new AppointmentLogSearchResponseDTO();

        List<AppointmentLogResponseDTO> appointmentLogSearchDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int APPOINTMENT_DATE_INDEX = 1;
            final int APPOINTMENT_NUMBER_INDEX = 2;
            final int APPOINTMENT_TIME_INDEX = 3;
            final int ESEWA_ID_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_GENDER_INDEX = 7;
            final int PATIENT_DOB_INDEX = 8;
            final int IS_REGISTERED_INDEX = 9;
            final int IS_SELF_INDEX = 10;
            final int PATIENT_MOBILE_NUMBER_INDEX = 11;
            final int SPECIALIZATION_NAME_INDEX = 12;
            final int TRANSACTION_NUMBER_INDEX = 13;
            final int APPOINTMENT_AMOUNT_INDEX = 14;
            final int DOCTOR_NAME_INDEX = 15;
            final int APPOINTMENT_STATUS_INDEX = 16;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            AppointmentLogResponseDTO appointmentLogResponseDTO =
                    AppointmentLogResponseDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber((result[REGISTRATION_NUMBER_INDEX] == null)
                                    ? null : result[REGISTRATION_NUMBER_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(AgeConverterUtils.calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogResponseDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentLogSearchResponseDTO.setAppointmentLogSearchDTOList(appointmentLogSearchDTOS);
        appointmentLogSearchResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentLogSearchResponseDTO;

    }


//    public static List<AppointmentPendingApprovalDTO> parseQueryResultToAppointmentApprovalResponse
//            (List<Object[]> results) {
//
//        List<AppointmentPendingApprovalDTO> appointmentPendingApprovalResponseDTOS = new ArrayList<>();
//
//        results.forEach(result -> {
//            final int HOSPITAL_NAME_INDEX = 0;
//            final int APPOINTMENT_DATE_INDEX = 1;
//            final int APPOINTMENT_NUMBER_INDEX = 2;
//            final int APPOINTMENT_TIME_INDEX = 3;
//            final int ESEWA_ID_INDEX = 4;
//            final int REGISTRATION_NUMBER_INDEX = 5;
//            final int PATIENT_NAME_INDEX = 6;
//            final int PATIENT_GENDER_INDEX = 7;
//            final int PATIENT_DOB_INDEX = 8;
//            final int IS_REGISTERED_INDEX = 9;
//            final int IS_SELF_INDEX = 10;
//            final int PATIENT_MOBILE_NUMBER_INDEX = 11;
//            final int SPECIALIZATION_NAME_INDEX = 12;
//            final int TRANSACTION_NUMBER_INDEX = 13;
//            final int APPOINTMENT_AMOUNT_INDEX = 14;
//            final int PATIENT_META_INFO_ID_INDEX = 15;
//
//
//            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
//            Date appointmentTime = (Date) result[APPOINTMENT_TIME_INDEX];
//            Date patientDob = (Date) result[PATIENT_DOB_INDEX];
//
//            AppointmentPendingApprovalDTO appointmentStatusResponseDTO = AppointmentPendingApprovalDTO.builder()
//                    .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
//                    .appointmentDate(appointmentDate)
//                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
//                    .appointmentTime(appointmentTime)
//                    .esewaId(result[ESEWA_ID_INDEX].toString())
//                    .registrationNumber((result[REGISTRATION_NUMBER_INDEX] == null) ? null : result[REGISTRATION_NUMBER_INDEX].toString())
//                    .patientName(result[PATIENT_NAME_INDEX].toString())
//                    .patientGender((Gender) result[PATIENT_GENDER_INDEX])
//                    .patientDob(patientDob)
//                    .patientAge(AgeConverterUtils.calculateAge(patientDob))
//                    .isRegistered((Character) result[IS_REGISTERED_INDEX])
//                    .isSelf((Character) result[IS_SELF_INDEX])
//                    .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
//                    .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
//                    .transactionNumber(result[TRANSACTION_NUMBER_INDEX].toString())
//                    .appointmentAmount(Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString()))
//                    .patientMetaInfoId(Long.parseLong(result[PATIENT_META_INFO_ID_INDEX].toString()))
//                    .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
//                    .build();
//
//            appointmentPendingApprovalResponseDTOS.add(appointmentStatusResponseDTO);
//        });

//          }


}
