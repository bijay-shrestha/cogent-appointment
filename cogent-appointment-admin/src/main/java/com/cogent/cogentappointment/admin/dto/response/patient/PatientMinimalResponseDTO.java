package com.cogent.cogentappointment.admin.dto.response.patient;

import com.cogent.cogentappointment.admin.enums.Gender;
import lombok.*;

import java.io.Serializable;
/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientMinimalResponseDTO implements Serializable {

    private Long patientId;

    private String name;

    private String mobileNumber;

    private Gender gender;

    private String age;

    private Integer totalItems;
}
