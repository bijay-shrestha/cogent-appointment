package com.cogent.cogentappointment.admin.dto.request.hospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rupak ON 2020/06/22-3:27 PM
 */
@Getter
@Setter
public class DepartmentCancelApprovalSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long hospitalId;

    private Long hospitalDepartmentId;

    private Long patientMetaInfoId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;
}
