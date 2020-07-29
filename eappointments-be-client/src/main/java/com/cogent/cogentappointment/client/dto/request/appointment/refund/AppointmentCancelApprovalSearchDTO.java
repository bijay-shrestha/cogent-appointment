package com.cogent.cogentappointment.client.dto.request.appointment.refund;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 07/02/2020
 */
@Getter
@Setter
public class AppointmentCancelApprovalSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;
}
