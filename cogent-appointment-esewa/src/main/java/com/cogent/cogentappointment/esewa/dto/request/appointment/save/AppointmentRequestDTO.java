package com.cogent.cogentappointment.esewa.dto.request.appointment.save;

import com.cogent.cogentappointment.esewa.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String createdDateNepali;

    @NotNull
    @Status
    private Character isFollowUp;

    private Long parentAppointmentId;

    @NotNull
    private Long appointmentReservationId;

    private Long hospitalAppointmentServiceTypeId;

}
