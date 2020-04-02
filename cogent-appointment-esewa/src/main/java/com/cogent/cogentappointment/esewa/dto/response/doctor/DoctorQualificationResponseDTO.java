package com.cogent.cogentappointment.esewa.dto.response.doctor;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 13/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorQualificationResponseDTO implements Serializable {

    private Long doctorQualificationId;

    private Long qualificationId;

    private String qualificationName;
}
