package com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 28/05/20
 */
@Getter
@Setter
public class AppointmentHospitalDeptCheckAvailabilityRequestDTO implements Serializable {

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Long hospitalDepartmentId;
}
