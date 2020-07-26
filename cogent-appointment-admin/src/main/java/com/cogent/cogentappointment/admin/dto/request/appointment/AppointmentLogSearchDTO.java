package com.cogent.cogentappointment.admin.dto.request.appointment;

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

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;

    private String appointmentNumber;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    /*NEW OR REGISTERED*/
    private Character patientType;

    /*SELF OR OTHERS*/
    private Character appointmentCategory;

    private String status;

    /*DOC = DOCTOR CONSULTATION
    * DEP = DEPARTMENT CONSULTATION*/
    private String appointmentServiceTypeCode;

    private Long hospitalDepartmentId;

    private Long hospitalDepartmentRoomInfoId;

}
