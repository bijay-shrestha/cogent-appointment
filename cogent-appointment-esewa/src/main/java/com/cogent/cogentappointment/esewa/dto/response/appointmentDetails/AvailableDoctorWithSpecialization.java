package com.cogent.cogentappointment.esewa.dto.response.appointmentDetails;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author smriti on 16/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDoctorWithSpecialization implements Serializable {

    private BigInteger doctorId;

    private String doctorName;

    private String doctorSalutation;

    private BigInteger specializationId;

    private String specializationName;

    private Character dayOffStatus;

    private String nmcNumber;

    private String qualificationAlias;

    private Double appointmentCharge;

    private String fileUri;
}
