package com.cogent.cogentappointment.esewa.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti on 05/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDTO implements Serializable {

    private int responseCode;

    private HttpStatus responseStatus;
}
