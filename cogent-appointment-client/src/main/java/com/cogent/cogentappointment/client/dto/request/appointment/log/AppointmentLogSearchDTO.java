package com.cogent.cogentappointment.client.dto.request.appointment.log;

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

    private Date fromDate;

    private Date toDate;

    private String appointmentNumber;

    private Long appointmentId;

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

}
