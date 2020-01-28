package com.cogent.cogentappointment.admin.dto.request.doctor;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-29
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String name;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String nmcNumber;

    @NotNull
    private Character genderCode;

    @NotNull
    private Long hospitalId;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    private Double appointmentCharge;
}
