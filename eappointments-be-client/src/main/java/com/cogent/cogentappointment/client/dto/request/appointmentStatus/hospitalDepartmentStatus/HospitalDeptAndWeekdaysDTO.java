package com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus;

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
