package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 07/02/2020
 */
@Getter
@Setter
public class AppointmentRefundSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long hospitalId;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;
}
