package com.cogent.cogentappointment.esewa.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti ON 18/12/2019
 */
@Getter
@Setter
@Builder
public class StatusResponseDTO implements Serializable {

    private int responseCode;

    private HttpStatus responseStatus;
}
