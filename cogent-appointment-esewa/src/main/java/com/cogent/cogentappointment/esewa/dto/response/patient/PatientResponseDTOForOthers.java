package com.cogent.cogentappointment.esewa.dto.response.patient;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 03/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTOForOthers implements Serializable {

    private Long parentPatientId;

    private List<PatientMinResponseDTOForOthers> patientInfo;
}
