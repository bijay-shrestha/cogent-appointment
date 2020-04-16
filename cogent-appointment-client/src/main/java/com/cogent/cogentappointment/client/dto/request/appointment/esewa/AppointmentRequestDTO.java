package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-10-22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDTO implements Serializable {

    @NotNull
    private Boolean isNewRegistration;

    private Long patientId;

    @NotNull
    @NotEmpty
    private String createdDateNepali;

    @NotNull
    @Status
    private Character isFollowUp;

    private Long parentAppointmentId;

    @NotNull
    private Long appointmentReservationId;
}
