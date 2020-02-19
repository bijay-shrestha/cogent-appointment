package com.cogent.cogentappointment.client.dto.request.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentLogSearchDTO implements Serializable {

    private String status;

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;

    private String appointmentNumber;

    private Long appointmentId;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    private Character patientType;

    private Character patientCategory;

    private String patientAddress;



}
