package com.cogent.cogentappointment.esewa.dto.request.appointment.history;

import com.cogent.cogentappointment.esewa.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 06/02/2020
 */
@Getter
@Setter
public class AppointmentSearchDTO implements Serializable {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    private String status;

    private Long hospitalId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String mobileNumber;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    @Status
    private Character isSelf;

    @NotNull
    private Long appointmentServiceTypeId;
}
