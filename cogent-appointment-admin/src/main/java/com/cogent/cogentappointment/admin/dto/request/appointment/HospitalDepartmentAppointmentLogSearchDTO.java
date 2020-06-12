package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 6/12/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDepartmentAppointmentLogSearchDTO implements Serializable {

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;

    private String appointmentNumber;

    private Long patientMetaInfoId;

    private Long hospitaDepartmentId;

    /*NEW OR REGISTERED*/
    private Character patientType;

    /*SELF OR OTHERS*/
    private Character appointmentCategory;

    private String status;

}
