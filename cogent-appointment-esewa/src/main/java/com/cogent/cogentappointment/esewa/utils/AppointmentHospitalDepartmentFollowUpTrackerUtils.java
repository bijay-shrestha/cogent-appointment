package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpResponseDTO;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 04/06/20
 */
public class AppointmentHospitalDepartmentFollowUpTrackerUtils {

    public static AppointmentHospitalDeptFollowUpResponseDTO parseAppointmentHospitalDeptFollowUpResponseDTO(
            Character isFollowUp,
            Double appointmentCharge,
            Long parentAppointmentId,
            Long savedAppointmentReservationId,
            Double hospitalRefundPercentage) {

        return AppointmentHospitalDeptFollowUpResponseDTO.builder()
                .isFollowUp(isFollowUp)
                .appointmentCharge(appointmentCharge)
                .parentAppointmentId(parentAppointmentId)
                .appointmentReservationId(savedAppointmentReservationId)
                .refundPercentage(hospitalRefundPercentage)
                .build();
    }

    public static void parseResponseStatus(AppointmentHospitalDeptFollowUpResponseDTO responseDTO) {

        responseDTO.setResponseCode(OK.value());
        responseDTO.setResponseStatus(OK);
    }
}
