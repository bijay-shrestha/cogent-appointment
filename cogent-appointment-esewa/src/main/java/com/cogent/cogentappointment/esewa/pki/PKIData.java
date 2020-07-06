package com.cogent.cogentappointment.esewa.pki;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 06/07/20
 */
@Getter
@Setter
public class PKIData implements Serializable {

    private String signature;

    @JsonProperty("secret_key")
    private String secretKey;

    private String data;
}
