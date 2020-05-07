package com.cogent.cogentappointment.admin.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO implements Serializable {

    //todo: rename as hospitalPatientInfoId
    private Long id;

    private String name;

    private String address;

    private String email;

    private String registrationNumber;

    private String mobileNumber;

    private String eSewaId;

    private Gender gender;

    private String age;

    private Character status;

    private String hospitalNumber;

    private String hospitalName;

    private Date dateOfBirth;

    private Character isRegistered;

    private Integer totalItems;
}
