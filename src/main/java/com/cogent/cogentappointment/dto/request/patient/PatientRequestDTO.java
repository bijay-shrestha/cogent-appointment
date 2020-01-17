package com.cogent.cogentappointment.dto.request.patient;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 16/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO implements Serializable {

    private Long patientId;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    private Character gender;

    @NotNull
    private Date dateOfBirth;

    @Email
    private String email;

    @Status
    private Character isSelf;

    @Status
    private Character isRegistered;

    @NotNull
    private String esewaId;

    @NotNull
    private String address;

    @NotNull
    private Long hospitalId;

    @NotNull
    private String title;

    @NotNull
    @Status
    private Character status;
}
