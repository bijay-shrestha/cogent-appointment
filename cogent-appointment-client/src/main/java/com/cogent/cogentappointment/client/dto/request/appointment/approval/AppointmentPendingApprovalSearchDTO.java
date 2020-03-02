package com.cogent.cogentappointment.client.dto.request.appointment.approval;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class AppointmentPendingApprovalSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long appointmentId;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    private Character patientType;
}
