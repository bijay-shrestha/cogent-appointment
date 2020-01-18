package com.cogent.cogentappointment.dto.response.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smriti ON 18/01/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMinimalResponseDTO implements Serializable {

    private String name;

    private String hisNumber;

    private String mobileNumber;

    private String address;

    private String city;

    private String country;

    private Character gender;

    private String age;

    private LocalDate dateOfBirth;

    private String code;

    private Character status;

    private String remarks;

    private String email;

    private Integer totalItems;

}
