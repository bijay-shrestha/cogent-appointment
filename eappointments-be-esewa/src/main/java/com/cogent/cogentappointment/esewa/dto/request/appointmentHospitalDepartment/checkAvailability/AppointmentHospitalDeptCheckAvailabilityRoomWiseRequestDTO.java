package com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 02/06/20
 */
@Getter
@Setter
public class AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO implements Serializable {

    @NotNull
    private Long hddRosterId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Long hospitalDepartmentId;

    @NotNull
    private Long hospitalDepartmentRoomInfoId;
}
