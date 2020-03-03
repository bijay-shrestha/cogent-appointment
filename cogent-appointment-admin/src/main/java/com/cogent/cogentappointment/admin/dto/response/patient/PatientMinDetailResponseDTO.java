package com.cogent.cogentappointment.admin.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientMinDetailResponseDTO implements Serializable {

    private String appointmentNumber;

    private String name;

    private String mobileNumber;

    private String address;

    private String age;

    private Gender gender;

    private Character isSelf;

}
