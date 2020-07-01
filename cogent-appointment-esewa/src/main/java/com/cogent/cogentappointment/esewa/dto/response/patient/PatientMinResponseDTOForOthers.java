package com.cogent.cogentappointment.esewa.dto.response.patient;

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
public class PatientMinResponseDTOForOthers implements Serializable {

    private Long hospitalPatientInfoId;

    private Long patientId;

    private String name;

    private String address;

    private String mobileNumber;

    private Date dateOfBirth;

    private String age;

    private String registrationNumber;

    private Gender gender;

    private String hospitalName;

    private Character hasAddress = 'N';

    private String province;

    private String district;

    private String vdcOrMunicipality;

    private String wardNumber;

    private Integer totalItems;
}
