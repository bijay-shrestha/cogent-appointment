package com.cogent.cogentappointment.client.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
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

    private String address;

    private String mobileNumber;

    private String age;

    private String registrationNumber;

    private Integer totalItems;
}
