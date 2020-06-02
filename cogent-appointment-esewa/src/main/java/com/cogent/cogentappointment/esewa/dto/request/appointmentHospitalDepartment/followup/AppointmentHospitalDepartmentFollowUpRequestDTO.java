package com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 02/06/20
 */
@Getter
@Setter
public class AppointmentHospitalDepartmentFollowUpRequestDTO implements Serializable {

    private Long patientId;

    @NotNull
    private Long hospitalDepartmentId;

    @NotNull
    private Long hospitalId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    @NotEmpty
    @NotBlank
    private String appointmentTime;

    @NotNull
    @NotEmpty
    @NotBlank
    private String userId;
}
