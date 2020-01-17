package com.cogent.cogentappointment.dto.request.patient;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Sauravi Thapa 9/22/19
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSearchRequestDTO implements Serializable {
    private Long id;

    private String metaInfo;

    private String firstName;

    private String code;

    @Status
    private Character status;

    private String address;

    private String country;

    private String city;

//    @Gender
    private Character gender;

    private String email;

    private String mobileNumber;

    private String emergencyContact;

    private String passportNumber;

    private String citizenshipNumber;

    private String pan;

    private String bloodGroup;

    private String education;

    private String hisNumber;

    private LocalDate dateOfBirth;

    private Long religionId;

    private Long maritalStatusId;

    private Long nationalityId;

    private Long municipalityId;

    private Long surnameId;

    private Long titleId;

    private Long categoryId;

    private String referredBy;

}
