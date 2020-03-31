package com.cogent.cogentappointment.client.dto.request.appointment;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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

    @NotNull
    private Long hospitalId;

    private Long patientId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    @NotEmpty
    private String appointmentTime;

    @NotNull
    @NotEmpty
    private String createdDateNepali;

    private Character isFollowUp;

    private Long parentAppointmentId;

    private Long appointmentReservationId;
}
