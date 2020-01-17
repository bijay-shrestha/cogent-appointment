package com.cogent.cogentappointment.dto.response.patient;

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
public class PatientResponseDTO implements Serializable {

    private String name;

    private String code;

    private String age;

    private Character status;

    private String address;

    private String country;

    private String city;

    private Character gender;

    private String mobileNumber;

    private String email;

    private String hisNumber;

    private String remarks;

    private String bloodGroup;

    private String citizenshipNumber;

    private LocalDate dateOfBirth;

    private String education;

    private String emergencyContact;

    private String pan;

    private String passportNumber;

    private String referredBy;

    private String maritalStatus;

    private String municipality;

    private String nationality;

    private String religion;

    private String ethnicity;

    private String category;

}
