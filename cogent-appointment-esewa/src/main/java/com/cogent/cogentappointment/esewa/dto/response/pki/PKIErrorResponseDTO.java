package com.cogent.cogentappointment.esewa.dto.response.pki;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 09/07/2020
 */
@Getter
@Setter
@Builder
public class PKIErrorResponseDTO implements Serializable {

    private int status;

    private String errorMessage;
}
