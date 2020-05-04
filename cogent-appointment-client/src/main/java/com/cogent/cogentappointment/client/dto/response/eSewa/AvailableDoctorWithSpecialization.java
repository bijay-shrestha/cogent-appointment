package com.cogent.cogentappointment.client.dto.response.eSewa;

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

    private BigInteger specializationId;

    private String specializationName;

//    private Character dayOffStatus;

    private String fileUri;

    private String qualificationAlias;

    private String nmcNumber;


}
