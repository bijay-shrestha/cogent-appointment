package com.cogent.cogentappointment.client.dto.request.doctor;

import com.cogent.cogentappointment.client.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 10)
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
