package com.cogent.cogentappointment.dto.request.doctor;

import com.cogent.cogentappointment.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-09-27
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequestDTO implements Serializable {

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
    @Status
    private Character status;

    @NotNull
    private Long hospitalId;

    @NotNull
    private Double appointmentCharge;

    @NotEmpty
    private List<Long> specializationIds;

    @NotEmpty
    private List<Long> qualificationIds;
}
