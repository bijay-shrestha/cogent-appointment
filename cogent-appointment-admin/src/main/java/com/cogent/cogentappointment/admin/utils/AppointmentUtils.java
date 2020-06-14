package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REJECTED;
import static com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils.calculateAge;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    public static AppointmentRefundDetail parseRefundRejectDetails(AppointmentRefundRejectDTO refundRejectDTO,
                                                AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRejectDTO.getRemarks());

        return refundDetail;
    }

    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient, Long followUp,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setPillType(pillType);
        countResponseDTO.setFollowUpPatient(followUp);

        return countResponseDTO;
    }

    public static void parseAppointmentRejectDetails(AppointmentRejectDTO rejectDTO,
                                                     Appointment appointment) {
        appointment.setStatus(REJECTED);
        appointment.setRemarks(rejectDTO.getRemarks());
    }

    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentStatusResponseDTOS
            (List<Object[]> results) {

        List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
            final int DOCTOR_ID_INDEX = 2;
            final int SPECIALIZATION_ID_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int GENDER_INDEX = 6;
            final int MOBILE_NUMBER_INDEX = 7;
            final int AGE_INDEX = 8;
            final int APPOINTMENT_ID_INDEX = 9;
            final int IS_FOLLOW_UP_INDEX = 10;
            final int HAS_TRANSFERRED_INDEX = 11;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];

            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();

            AppointmentStatusResponseDTO appointmentStatusResponseDTO = AppointmentStatusResponseDTO.builder()
                    .date(appointmentLocalDate)
                    .appointmentTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
                    .doctorId(Objects.isNull(result[DOCTOR_ID_INDEX]) ? null :
                            Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
                    .specializationId(Objects.isNull(result[SPECIALIZATION_ID_INDEX]) ? null :
                            Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                    .patientName(result[PATIENT_NAME_INDEX].toString())
                    .mobileNumber(result[MOBILE_NUMBER_INDEX].toString())
                    .age(result[AGE_INDEX].toString())
                    .gender(result[GENDER_INDEX].toString())
                    .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                    .isFollowUp((Character) result[IS_FOLLOW_UP_INDEX])
                    .hasTransferred((Character) result[HAS_TRANSFERRED_INDEX])
                    .build();

            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return appointmentStatusResponseDTOS;
    }

    public static AppointmentLogResponseDTO parseQueryResultToAppointmentLogResponse(List<Object[]> results) {

        AppointmentLogResponseDTO appointmentLogResponseDTO = new AppointmentLogResponseDTO();

        List<AppointmentLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

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
            final int PATIENT_MOBILE_NUMBER_INDEX = 10;
            final int SPECIALIZATION_NAME_INDEX = 11;
            final int TRANSACTION_NUMBER_INDEX = 12;
            final int APPOINTMENT_AMOUNT_INDEX = 13;
            final int DOCTOR_NAME_INDEX = 14;
            final int APPOINTMENT_STATUS_INDEX = 15;
            final int REFUND_AMOUNT_INDEX = 16;
            final int PATIENT_ADDRESS_INDEX = 17;
            final int TRANSACTION_DATE_INDEX = 18;
            final int APPOINTMENT_MODE_INDEX = 19;
            final int IS_FOLLOW_UP_INDEX = 20;
            final int REVENUE_AMOUNT_INDEX = 21;

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
                            .esewaId(Objects.isNull(result[ESEWA_ID_INDEX]) ? null : result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .refundAmount(refundAmount)
                            .patientAddress(Objects.isNull(result[PATIENT_ADDRESS_INDEX]) ? "" :
                                    result[PATIENT_ADDRESS_INDEX].toString())
                            .transactionDate((Date) result[TRANSACTION_DATE_INDEX])
                            .appointmentMode(result[APPOINTMENT_MODE_INDEX].toString())
                            .isFollowUp(result[IS_FOLLOW_UP_INDEX].toString().charAt(0))
                            .revenueAmount(Double.parseDouble(result[REVENUE_AMOUNT_INDEX].toString()))
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogDTO);

        });

        appointmentLogResponseDTO.setAppointmentLogs(appointmentLogSearchDTOS);
        appointmentLogResponseDTO.setTotalItems(appointmentLogSearchDTOS.size());

        return appointmentLogResponseDTO;
    }

    public static Map<String, List<AppointmentQueueDTO>> parseQueryResultToAppointmentQueueForTodayByTimeResponse(List<Object[]> results) {

        List<AppointmentQueueSearchByTimeDTO> appointmentQueueSearchByTimeDTOS = new ArrayList<>();

        AppointmentQueueDTO appointmentQueueSearchDTO = new AppointmentQueueDTO();

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

//        appointmentQueueSearchDTO.setAppointmentQueueByTimeDTOList(appointmentQueueByTimeDTOS);
        appointmentQueueSearchDTO.setTotalItems(appointmentQueueByTimeDTOS.size());

        //group by price
        Map<String, List<AppointmentQueueDTO>> groupByPriceMap =
                appointmentQueueByTimeDTOS.stream().collect(Collectors.groupingBy(AppointmentQueueDTO::getAppointmentTime));

        return groupByPriceMap;

    }

    public static List<HospitalDeptAppointmentStatusResponseDTO> parseQueryResultToHospitalDeptAppointmentStatusResponseDTOS
            (List<Object[]> results) {

        List<HospitalDeptAppointmentStatusResponseDTO> HospitalDeptAppointmentStatusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int APPOINTMENT_DATE_INDEX = 0;
            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
            final int HOSPITAL_DEPARTMENT_ID_INDEX = 2;
            final int HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX = 3;
            final int APPOINTMENT_NUMBER_INDEX = 4;
            final int PATIENT_NAME_INDEX = 5;
            final int GENDER_INDEX = 6;
            final int MOBILE_NUMBER_INDEX = 7;
            final int AGE_INDEX = 8;
            final int APPOINTMENT_ID_INDEX = 9;
            final int IS_FOLLOW_UP_INDEX = 10;
            final int HAS_TRANSFERRED_INDEX = 11;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];

            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();

            HospitalDeptAppointmentStatusResponseDTO appointmentStatusResponseDTO = HospitalDeptAppointmentStatusResponseDTO.builder()
                    .date(appointmentLocalDate)
                    .appointmentTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
                    .departmentId(Long.parseLong(result[HOSPITAL_DEPARTMENT_ID_INDEX].toString()))
                    .roomId((Objects.isNull(result[HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX]))?
                            null:Long.parseLong(result[HOSPITAL_DEPARTMENT_ROOM_INFO_ID_INDEX].toString()))
                    .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                    .patientName(result[PATIENT_NAME_INDEX].toString())
                    .mobileNumber(result[MOBILE_NUMBER_INDEX].toString())
                    .age(result[AGE_INDEX].toString())
                    .gender(result[GENDER_INDEX].toString())
                    .appointmentId(Long.parseLong(result[APPOINTMENT_ID_INDEX].toString()))
                    .isFollowUp((Character) result[IS_FOLLOW_UP_INDEX])
                    .hasTransferred((Character) result[HAS_TRANSFERRED_INDEX])
                    .build();

            HospitalDeptAppointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
        });

        return HospitalDeptAppointmentStatusResponseDTOS;
    }

}
