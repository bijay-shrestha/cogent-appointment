package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentLog.AppointmentLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.calculateAge;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static Appointment parseToAppointment(AppointmentRequestDTO requestDTO,
                                                 String appointmentNumber,
                                                 Patient patient,
                                                 Specialization specialization,
                                                 Doctor doctor,
                                                 Hospital hospital) {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setAppointmentTime(parseAppointmentTime(
                requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime()));
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setSerialNumber(generateRandomNumber(6));
        appointment.setCreatedDateNepali(requestDTO.getCreatedDateNepali());
        appointment.setIsFreeFollowUp(requestDTO.getIsFreeFollowUp());
        appointment.setStatus(PENDING_APPROVAL);
        parseToAppointment(patient, specialization, doctor, hospital, appointment);
        return appointment;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

    private static void parseToAppointment(Patient patient,
                                           Specialization specialization,
                                           Doctor doctor,
                                           Hospital hospital,
                                           Appointment appointment) {
        appointment.setDoctorId(doctor);
        appointment.setSpecializationId(specialization);
        appointment.setHospitalId(hospital);
        appointment.setPatientId(patient);
    }

    public static AppointmentSuccessResponseDTO parseToAppointmentSuccessResponseDTO(String appointmentNumber) {
        return AppointmentSuccessResponseDTO.builder()
                .appointmentNumber(appointmentNumber)
                .appointmentTransactionStatus(ACTIVE)
                .build();
    }

    public static void parseAppointmentCancelledDetails(Appointment appointment,
                                                        String remarks) {

        appointment.setRemarks(remarks);
        appointment.setStatus(CANCELLED);
    }

    public static AppointmentRefundDetail parseToAppointmentRefundDetail(Appointment appointment,
                                                                         Double refundAmount) {

        AppointmentRefundDetail refundDetail = new AppointmentRefundDetail();
        refundDetail.setAppointmentId(appointment);
        refundDetail.setRefundAmount(refundAmount);
        refundDetail.setStatus(PENDING_APPROVAL);
        refundDetail.setCancelledDate(new Date());
        return refundDetail;
    }

    public static String generateAppointmentNumber(List results) {
        return results.isEmpty() ? "0001" :
                String.format("%04d", Integer.parseInt(results.get(0).toString()) + 1);
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

    public static AppointmentCheckAvailabilityResponseDTO parseToAvailabilityResponse(
            String startTime,
            String endTime,
            Date queryDate,
            List<String> availableTimeSlots) {

        return AppointmentCheckAvailabilityResponseDTO.builder()
                .queryDate(queryDate)
                .doctorAvailableTime(startTime + HYPHEN + endTime)
                .availableTimeSlots(availableTimeSlots)
                .build();
    }

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getAppointmentTime().equals(date));
    }

    public static void parseToRescheduleAppointment(Appointment appointment,
                                                    AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        appointment.setAppointmentDate(rescheduleRequestDTO.getRescheduleDate());
        appointment.setAppointmentTime(parseAppointmentTime(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime()));
        appointment.setRemarks(rescheduleRequestDTO.getRemarks());
    }

    public static AppointmentRescheduleLog parseToAppointmentRescheduleLog(
            Appointment appointment,
            AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        AppointmentRescheduleLog appointmentRescheduleLog = new AppointmentRescheduleLog();
        appointmentRescheduleLog.setAppointmentId(appointment);
        appointmentRescheduleLog.setPreviousAppointmentDate(appointment.getAppointmentDate());
        appointmentRescheduleLog.setRescheduleDate(rescheduleRequestDTO.getRescheduleDate());
        appointmentRescheduleLog.setRemarks(rescheduleRequestDTO.getRemarks());
        appointmentRescheduleLog.setStatus(RESCHEDULED);
        return appointmentRescheduleLog;
    }

    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setPillType(pillType);

        return countResponseDTO;
    }

    public static void parseRefundRejectDetails(AppointmentRefundRejectDTO refundRejectDTO,
                                                AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRejectDTO.getRemarks());
    }

    public static void parseAppointmentRejectDetails(AppointmentRejectDTO rejectDTO,
                                                     Appointment appointment) {
        appointment.setStatus(REJECTED);
        appointment.setRemarks(rejectDTO.getRemarks());
    }

    public static AppointmentRescheduleLogResponseDTO parseQueryResultToAppointmentRescheduleLogResponse(List<Object[]> results) {

        AppointmentRescheduleLogResponseDTO rescheduleLogResponseDTO = new AppointmentRescheduleLogResponseDTO();

        List<AppointmentRescheduleLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int ESEWA_ID_INDEX = 1;
            final int PREVIOUS_APPOINTMENT_DATE_INDEX = 2;
            final int APPOINTMENT_RESCHEDULED_DATE_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_DOB_INDEX = 7;
            final int PATIENT_GENDER_INDEX = 8;
            final int PATIENT_MOBILE_NUMBER_INDEX = 9;
            final int SPECIALIZATION_NAME_INDEX = 10;
            final int DOCTOR_NAME_INDEX = 11;
            final int TRANSACTION_NUMBER_INDEX = 12;
            final int APPOINTMENT_AMOUNT_INDEX = 13;
            final int REMARKS_INDEX = 14;


            Date previosAppointmentDate = (Date) result[PREVIOUS_APPOINTMENT_DATE_INDEX];
            Date rescheduledAppointmentDate = (Date) result[APPOINTMENT_RESCHEDULED_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    "" : result[REGISTRATION_NUMBER_INDEX].toString();

            String remarks = Objects.isNull(result[REMARKS_INDEX]) ?
                    null : result[REMARKS_INDEX].toString();

            AppointmentRescheduleLogDTO appointmentRescheduleLogDTO =
                    AppointmentRescheduleLogDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .previousAppointmentDate(previosAppointmentDate)
                            .rescheduleAppointmentDate(rescheduledAppointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientAge(calculateAge(patientDob))
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .remarks(remarks)
                            .build();

            appointmentLogSearchDTOS.add(appointmentRescheduleLogDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        rescheduleLogResponseDTO.setAppointmentRescheduleLogDTOS(appointmentLogSearchDTOS);
        rescheduleLogResponseDTO.setTotalAmount(totalAmount.get());

        return rescheduleLogResponseDTO;

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
            final int REFUND_AMOUNT_INDEX = 16;
            final int APPOINTMENT_ID_INDEX = 17;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            Double refundAmount = Objects.isNull(result[REFUND_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REFUND_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    null : result[REGISTRATION_NUMBER_INDEX].toString();

            AppointmentPendingApprovalDTO appointmentStatusResponseDTO =
                    AppointmentPendingApprovalDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .refundAmount(refundAmount)
                            .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                            .build();

            appointmentPendingApprovalDTOS.add(appointmentStatusResponseDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentPendingApprovalResponseDTO.setPendingAppointmentApprovals(appointmentPendingApprovalDTOS);
        appointmentPendingApprovalResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentPendingApprovalResponseDTO;
    }

    public static AppointmentLogResponseDTO parseQueryResultToAppointmentLogResponse(List<Object[]> results) {

        AppointmentLogResponseDTO appointmentLogResponseDTO = new AppointmentLogResponseDTO();

        List<AppointmentLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

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
            final int REFUND_AMOUNT_INDEX = 17;
            final int PATIENT_ADDRESS_INDEX = 18;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            Double refundAmount = Objects.isNull(result[REFUND_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REFUND_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    null : result[REGISTRATION_NUMBER_INDEX].toString();


            AppointmentLogDTO appointmentLogDTO =
                    AppointmentLogDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .isSelf((Character) result[IS_SELF_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .refundAmount(refundAmount)
                            .patientAddress(result[PATIENT_ADDRESS_INDEX].toString())
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogDTO);

            totalAmount.updateAndGet(v -> v + appointmentAmount);
        });

        appointmentLogResponseDTO.setAppointmentLogs(appointmentLogSearchDTOS);
        appointmentLogResponseDTO.setTotalAmount(totalAmount.get());

        return appointmentLogResponseDTO;

    }

}
