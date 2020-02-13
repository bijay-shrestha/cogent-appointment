package com.cogent.cogentappointment.client.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO implements Serializable {

    private String name;

    private Gender gender;

    private String address;

    private String email;

    private String registrationNumber;

    private String mobileNumber;

    private String eSewaId;

    private String age;

    private Character status;

    private String hospitalNumber;

    private String hospitalName;

    private Date dateOfBirth;

    private Character isSelf;

    private Character isRegistered;

}
