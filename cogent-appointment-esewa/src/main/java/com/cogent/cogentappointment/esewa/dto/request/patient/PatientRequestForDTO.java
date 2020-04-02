package com.cogent.cogentappointment.esewa.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/02/20
 */
@Getter
@Setter
public class PatientRequestForDTO implements Serializable {

    private String name;

    private String mobileNumber;

    private Character gender;

    private Date dateOfBirth;

    private String email;

    private String address;
}
