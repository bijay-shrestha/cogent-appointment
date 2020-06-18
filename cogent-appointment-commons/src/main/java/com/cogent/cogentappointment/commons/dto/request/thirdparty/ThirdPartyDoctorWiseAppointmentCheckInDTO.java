package com.cogent.cogentappointment.commons.dto.request.thirdparty;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 18/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyDoctorWiseAppointmentCheckInDTO implements Serializable {

    private String name;

    private int age;

    private int ageMonth;

    private int ageDay;

    private Gender gender;

    private String sex;

    private String district;

    private String vdc;

    private String wardNo;

    private String address;

    private String phoneNo;

    private String mobileNo;

    private String emailAddress;

    private String doctorName;

    private String specializationName;

    private String appointmentNo;

    private String patientId;
}
