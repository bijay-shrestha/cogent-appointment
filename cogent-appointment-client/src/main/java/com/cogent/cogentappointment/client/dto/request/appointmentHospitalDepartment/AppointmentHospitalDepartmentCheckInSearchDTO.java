package com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/06/20
 */
@Getter
@Setter
public class AppointmentHospitalDepartmentCheckInSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long appointmentId;

    private Long patientMetaInfoId;

    private Long hospitalDepartmentId;

    private Character patientType;

    private String appointmentNumber;
}
