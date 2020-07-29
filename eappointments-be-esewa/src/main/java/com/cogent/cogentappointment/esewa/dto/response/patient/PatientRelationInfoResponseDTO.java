package com.cogent.cogentappointment.esewa.dto.response.patient;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 03/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRelationInfoResponseDTO implements Serializable {

    private Long parentPatientId;

    private Long childPatientId;
}
