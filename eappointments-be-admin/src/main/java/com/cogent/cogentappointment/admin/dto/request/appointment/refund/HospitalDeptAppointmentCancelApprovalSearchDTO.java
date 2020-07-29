package com.cogent.cogentappointment.admin.dto.request.appointment.refund;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 29/06/2020
 */
@Getter
@Setter
public class HospitalDeptAppointmentCancelApprovalSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long hospitalId;

    private Long patientMetaInfoId;

    private Long hospitalDepartmentId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;
}
