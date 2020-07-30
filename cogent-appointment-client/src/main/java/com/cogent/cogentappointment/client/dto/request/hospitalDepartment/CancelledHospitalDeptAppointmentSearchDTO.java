package com.cogent.cogentappointment.client.dto.request.hospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rupak ON 2020/06/22-3:27 PM
 */
@Getter
@Setter
public class CancelledHospitalDeptAppointmentSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long hospitalDepartmentId;

    private Long hospitalDepartmentRoomInfoId;

    private Long roomId;

    private Long patientMetaInfoId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;
}
