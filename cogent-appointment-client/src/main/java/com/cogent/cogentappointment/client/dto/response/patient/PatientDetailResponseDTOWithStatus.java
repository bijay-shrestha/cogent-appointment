package com.cogent.cogentappointment.client.dto.response.patient;

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

    PatientDetailResponseDTO detailResponseDTO;

    private int responseCode;

    private HttpStatus responseStatus;

}
