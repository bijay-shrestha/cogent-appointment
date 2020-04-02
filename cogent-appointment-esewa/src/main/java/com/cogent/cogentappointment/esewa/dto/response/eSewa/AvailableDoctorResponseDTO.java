package com.cogent.cogentappointment.esewa.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 16/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDoctorResponseDTO implements Serializable {

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;
}
