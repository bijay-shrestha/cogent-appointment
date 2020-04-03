package com.cogent.cogentappointment.esewa.dto.response.hospital;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 29/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalMinResponseDTOWithStatus implements Serializable {
    List<HospitalMinResponseDTO> hospitalMinResponseDTOS;

    private int responseCode;

    private HttpStatus responseStatus;
}
