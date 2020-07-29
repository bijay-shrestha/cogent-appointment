package com.cogent.cogentappointment.esewa.dto.response.doctor;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti ON 04/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorMinResponseDTO implements Serializable {

    private BigInteger doctorId;

    private String doctorName;

    private String doctorSalutation;

    private BigInteger specializationId;

    private String specializationName;

    private String fileUri;

    private String qualificationAlias;

    private String nmcNumber;

    private Double appointmentCharge;
}
