package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentTimeDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.persistence.model.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
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

    public static AppointmentQueueSearchDTO parseQueryResultToAppointmentQueueForTodayResponse(List<Object[]> results) {

        AppointmentQueueSearchDTO appointmentQueueSearchDTO = new AppointmentQueueSearchDTO();

        List<AppointmentQueueDTO> appointmentQueueByTimeDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int APPOINTMENT_TIME_INDEX = 0;
            final int DOCTOR_NAME_INDEX = 1;
            final int PATIENT_NAME_INDEX = 2;
            final int PATIENT_MOBILE_NUMBER_INDEX = 3;
            final int SPECIALIZATION_NAME_INDEX = 4;
            final int DOCTOR_AVATAR_INDEX = 5;

            AppointmentQueueDTO appointmentQueueDTO =
                    AppointmentQueueDTO.builder()
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientMobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .doctorAvatar(result[DOCTOR_AVATAR_INDEX].toString())
                            .build();

            appointmentQueueByTimeDTOS.add(appointmentQueueDTO);

        });

        appointmentQueueSearchDTO.setAppointmentQueueByTimeDTOList(appointmentQueueByTimeDTOS);

        return appointmentQueueSearchDTO;

    }

    public static Map<String, List<AppointmentQueueDTO>> parseQueryResultToAppointmentQueueForTodayByTimeResponse(List<Object[]> results) {

        List<AppointmentQueueSearchByTimeDTO> appointmentQueueSearchByTimeDTOS = new ArrayList<>();

        AppointmentQueueSearchDTO appointmentQueueSearchDTO = new AppointmentQueueSearchDTO();

        List<AppointmentQueueDTO> appointmentQueueByTimeDTOS = new ArrayList<>();

        AtomicReference<Double> totalAmount = new AtomicReference<>(0D);

        results.forEach(result -> {
            final int APPOINTMENT_TIME_INDEX = 0;
            final int DOCTOR_NAME_INDEX = 1;
            final int SPECIALIZATION_NAME_INDEX = 2;
            final int PATIENT_NAME_INDEX = 3;
            final int PATIENT_MOBILE_NUMBER_INDEX = 4;
            final int DOCTOR_AVATAR_INDEX = 5;

            AppointmentTimeDTO appointmentTimeDTO = AppointmentTimeDTO.builder()
                    .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                    .build();

            AppointmentQueueDTO appointmentQueueByTimeDTO =
                    AppointmentQueueDTO.builder()
                            .appointmentTime(appointmentTimeDTO.getAppointmentTime())
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientMobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .doctorAvatar(result[DOCTOR_AVATAR_INDEX].toString())
                            .build();

            appointmentQueueByTimeDTOS.add(appointmentQueueByTimeDTO);

        });

        appointmentQueueSearchDTO.setAppointmentQueueByTimeDTOList(appointmentQueueByTimeDTOS);

        //group by price
        Map<String, List<AppointmentQueueDTO>> groupByPriceMap =
                appointmentQueueByTimeDTOS.stream().collect(Collectors.groupingBy(AppointmentQueueDTO::getAppointmentTime));

        return groupByPriceMap;

    }



}
