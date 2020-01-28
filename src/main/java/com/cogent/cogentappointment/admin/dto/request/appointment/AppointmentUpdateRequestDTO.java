package com.cogent.cogentappointment.admin.dto.request.appointment;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
public class AppointmentUpdateRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientTypeId;

    @NotNull
    private Long patientId;

    @NotNull
    private Long appointmentTypeId;

    @NotNull
    private Long appointmentModeId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long billTypeId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    @NotNull
    private Character emergency;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    @Status
    private Character status;

    private String referredBy;

    private String reason;
}
