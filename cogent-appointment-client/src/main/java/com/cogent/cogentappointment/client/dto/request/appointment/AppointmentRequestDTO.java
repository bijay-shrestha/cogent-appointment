package com.cogent.cogentappointment.client.dto.request.appointment;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
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

    private PatientRequestDTO patientRequestDTO;

    private Boolean IsNewRegistration;

    private Long patientId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String createdDateNepali;
}
