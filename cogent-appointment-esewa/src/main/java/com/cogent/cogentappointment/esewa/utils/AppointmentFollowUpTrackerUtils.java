package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTOWithStatus;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 16/02/20
 */
public class AppointmentFollowUpTrackerUtils {

    public static AppointmentFollowUpResponseDTO parseToAppointmentFollowUpResponseDTO(
            Character isFollowUp,
            Double appointmentCharge,
            Long parentAppointmentId,
            Long savedAppointmentReservationId) {

        return AppointmentFollowUpResponseDTO.builder()
                .isFollowUp(isFollowUp)
                .appointmentCharge(appointmentCharge)
                .parentAppointmentId(parentAppointmentId)
                .appointmentReservationId(savedAppointmentReservationId)
                .build();

    }

    public static AppointmentFollowUpResponseDTOWithStatus parseToAppointmentFollowUpResponseDTOWithStatus(
            AppointmentFollowUpResponseDTO responseDTO) {

        return AppointmentFollowUpResponseDTOWithStatus.builder()
                .responseDTO(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();

    }

}
