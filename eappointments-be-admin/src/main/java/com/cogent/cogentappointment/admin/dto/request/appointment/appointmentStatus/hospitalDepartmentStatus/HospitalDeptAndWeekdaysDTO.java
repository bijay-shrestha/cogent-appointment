package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/9/20
 */
@Getter
@Setter
public class HospitalDeptAndWeekdaysDTO implements Serializable {

    private Long hospitalDepartmentId;

    private String weekDay;
}
