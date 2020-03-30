package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTOWithStatus;
import com.cogent.cogentappointment.persistence.model.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
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
                .isFreeFollowUp(isFollowUp)
                .appointmentCharge(appointmentCharge)
                .parentAppointmentId(parentAppointmentId)
                .appointmentReservationId(savedAppointmentReservationId)
                .build();

    }

    public static void updateNumberOfFreeFollowUps(AppointmentFollowUpTracker followUpTracker) {
        followUpTracker.setRemainingNumberOfFollowUps(followUpTracker.getRemainingNumberOfFollowUps() - 1);

        if (followUpTracker.getRemainingNumberOfFollowUps() <= 0)
            followUpTracker.setStatus(INACTIVE);
    }

    public static AppointmentFollowUpTracker parseToAppointmentFollowUpTracker(Long parentAppointmentId,
                                                                               String parentAppointmentNumber,
                                                                               Integer remainingFollowUpCount,
                                                                               Doctor doctor,
                                                                               Specialization specialization,
                                                                               Patient patient,
                                                                               Hospital hospital) {

        AppointmentFollowUpTracker followUpTracker = new AppointmentFollowUpTracker();
        followUpTracker.setDoctorId(doctor);
        followUpTracker.setPatientId(patient);
        followUpTracker.setSpecializationId(specialization);
        followUpTracker.setHospitalId(hospital);
        followUpTracker.setParentAppointmentId(parentAppointmentId);
        followUpTracker.setParentAppointmentNumber(parentAppointmentNumber);
        followUpTracker.setRemainingNumberOfFollowUps(remainingFollowUpCount);
        followUpTracker.setAppointmentApprovedDate(new Date());
        followUpTracker.setStatus(ACTIVE);
        return followUpTracker;
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
