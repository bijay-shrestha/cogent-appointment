package com.cogent.cogentappointment.esewa.dto.response.patient;

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
public class PatientDetailResponseDTO implements Serializable {

    private Long patientId;

    private String name;

    private String mobileNumber;

    private String address;

    private String email;

    private Gender gender;

    private Date dateOfBirth;

    private String age;

    private String registrationNumber;

    private Character hasAddress = 'N';

    private String province;

    private String district;

    private String vdcOrMunicipality;

    private String wardNumber;

}
