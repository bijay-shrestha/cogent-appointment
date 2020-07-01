package com.cogent.cogentappointment.esewa.dto.response.patient;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDetailResponseDTOWithStatus implements Serializable {

    private PatientDetailResponseDTO detailResponseDTO;

    private int responseCode;

    private HttpStatus responseStatus;

}
