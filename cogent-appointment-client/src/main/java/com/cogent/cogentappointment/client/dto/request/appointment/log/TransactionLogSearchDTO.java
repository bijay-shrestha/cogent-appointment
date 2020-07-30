package com.cogent.cogentappointment.client.dto.request.appointment.log;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLogSearchDTO implements Serializable {

    private String status;

    private Date fromDate;

    private Date toDate;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    /*NEW OR REGISTERED*/
    private Character patientType;

    /*SELF OR OTHERS*/
    private Character appointmentCategory;

    private String transactionNumber;

    /*DOC = DOCTOR CONSULTATION
 * DEP = DEPARTMENT CONSULTATION*/
    private String appointmentServiceTypeCode;

    private Long hospitalDepartmentId;

    private String appointmentNumber;

    private Long hospitalDepartmentRoomInfoId;
}
