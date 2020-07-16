package com.cogent.cogentappointment.client.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti on 2019-09-27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorMinimalResponseDTO implements Serializable {

    private BigInteger id;

    private String doctorName;

    private String doctorSalutation;

    private String code;

    private String mobileNumber;

    private Character status;

    private String specializationName;

    private String fileUri;

    private int totalItems;
}
