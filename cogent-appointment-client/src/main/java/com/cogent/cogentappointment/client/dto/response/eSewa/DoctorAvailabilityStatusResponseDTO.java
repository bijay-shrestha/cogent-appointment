package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAvailabilityStatusResponseDTO implements Serializable {

    private Long count;

    private String status;

    private String message;

    private int responseCode;

    private HttpStatus responseStatus;
}
