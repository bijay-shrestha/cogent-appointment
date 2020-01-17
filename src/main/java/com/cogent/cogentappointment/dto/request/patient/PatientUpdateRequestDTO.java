package com.cogent.cogentappointment.dto.request.patient;

import com.cogent.cogentappointment.constraintvalidator.Status;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Sauravi Thapa 9/22/19
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class PatientUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String firstName;

    private String middleName;

    @NotNull
    private Long surnameId;

    @NotNull
    @NotBlank
    @NotEmpty
    private String code;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotBlank
    @NotEmpty
    private String address;

    @NotNull
    @NotBlank
    @NotEmpty
    private String country;

    private String city;

    @NotNull
    @NotBlank
    @NotEmpty
    private String remarks;

    @NotNull
//    @Gender
    private Character gender;

    @Email
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    private String mobileNumber;

    private String emergencyContact;

    private String passportNumber;

    private String citizenshipNumber;

    private String pan;

    private String bloodGroup;

    private String education;

    private LocalDate dateOfBirth;

    private Long religionId;

    private Long maritalStatusId;

    private Long nationalityId;

    private Long municipalityId;

    private Long titleId;

    private Long categoryId;

    private String referredBy;
}
