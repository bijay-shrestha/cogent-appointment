package com.cogent.cogentappointment.esewa.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti ON 29/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalMinResponseDTO implements Serializable {

    private BigInteger hospitalId;

    private String name;

    private String address;

    private String hospitalLogo;

    private String hospitalBanner;

    private String contactNumber;

    private String hospitalCode;
}
