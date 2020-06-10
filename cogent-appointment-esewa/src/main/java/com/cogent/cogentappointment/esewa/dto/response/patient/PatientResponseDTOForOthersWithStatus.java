package com.cogent.cogentappointment.esewa.dto.response.patient;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTOForOthersWithStatus implements Serializable {

    PatientResponseDTOForOthers minResponseDTOForOthers;

    private int responseCode;

    private HttpStatus responseStatus;
}
