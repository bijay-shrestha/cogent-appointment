package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;

/**
 * @author smriti on 16/02/20
 */
public class AppointmentFollowUpTrackerUtils {

    public static AppointmentFollowUpResponseDTO parseToAppointmentFollowUpResponseDTO(
            Character isFollowUp,
            Double followUpAppointmentCharge,
            Long parentAppointmentId,
            Long savedAppointmentReservationId) {

        return AppointmentFollowUpResponseDTO.builder()
                .isFreeFollowUp(isFollowUp)
                .followUpAppointmentCharge(followUpAppointmentCharge)
                .parentAppointmentId(parentAppointmentId)
                .appointmentReservationId(savedAppointmentReservationId)
                .build();

    }
}
