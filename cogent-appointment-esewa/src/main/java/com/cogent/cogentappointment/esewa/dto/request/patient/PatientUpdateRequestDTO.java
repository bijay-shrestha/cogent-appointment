package com.cogent.cogentappointment.esewa.dto.request.patient;

import com.cogent.cogentappointment.esewa.constraintvalidator.Status;
import com.cogent.cogentappointment.persistence.enums.Gender;
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
public class PatientUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    private String address;

    @NotNull
    private Gender gender;

    @NotNull
    @NotEmpty
    private String hospitalNumber;

    @Email
    private String email;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;
}
