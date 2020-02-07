package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 03/02/2020
 */
@Getter
@Setter
public class AppointmentPendingApprovalSearchDTO implements Serializable {

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;

    private Long doctorId;

    private Long specializationId;

    /*NEW OR REGISTERED*/
    private Character patientType;

    /*SELF OR OTHERS*/
    private Character patientCategory;
}
