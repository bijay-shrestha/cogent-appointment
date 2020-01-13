package com.cogent.cogentappointment.dto.response.doctor;

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
public class DoctorSpecializationResponseDTO implements Serializable {

    private Long doctorSpecializationId;

    private Long specializationId;

    private String specializationName;
}
