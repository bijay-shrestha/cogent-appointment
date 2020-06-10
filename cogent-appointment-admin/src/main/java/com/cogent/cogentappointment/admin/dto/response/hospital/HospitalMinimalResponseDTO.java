package com.cogent.cogentappointment.admin.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalMinimalResponseDTO implements Serializable {

    private BigInteger id;

    private String name;

    private String esewaMerchantCode;

    private String address;

    private Character status;

    private String fileUri;

    private int totalItems;
}
