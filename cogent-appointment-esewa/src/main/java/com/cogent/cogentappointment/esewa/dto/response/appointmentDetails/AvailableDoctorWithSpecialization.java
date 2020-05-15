package com.cogent.cogentappointment.esewa.dto.response.appointmentDetails;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 16/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDoctorWithSpecialization implements Serializable {

    private Long doctorId;

    private String nmcNumber;

    private String doctorName;

    private String doctorSalutation;

    private String fileUri;

    private String qualificationAlias;

    private Long specializationId;

    private String specializationName;

    private Character dayOffStatus;
}
