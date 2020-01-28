package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 2019-10-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchRequestDTO implements Serializable {

    private Long patientTypeId;

    private String patientMetaInfo;

    private Long appointmentTypeId;

    private Long appointmentModeId;

    private Long doctorId;

    private Long billType;

    private String appointmentNumber;

    private Character status;
}
