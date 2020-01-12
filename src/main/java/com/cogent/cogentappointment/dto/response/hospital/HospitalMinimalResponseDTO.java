package com.cogent.cogentappointment.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Rupak
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalMinimalResponseDTO implements Serializable {

    private BigInteger id;

    private String name;

    private String address;

    private Character status;

    private String fileUri;

    private int totalItems;
}
