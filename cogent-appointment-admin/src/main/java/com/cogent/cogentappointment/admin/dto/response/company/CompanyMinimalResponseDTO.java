package com.cogent.cogentappointment.admin.dto.response.company;

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
public class CompanyMinimalResponseDTO implements Serializable {

    private BigInteger id;

    private String name;

    private String companyCode;

    private String address;

    private Character status;

    private String fileUri;

    private int totalItems;
}
