package com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 02/06/20
 */
@Getter
@Setter
public class AppointmentHospitalDeptFollowUpRequestDTO implements Serializable {

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long hospitalDepartmentId;

    /*id of assigned room to hospital department -> HospitalDepartmentRoomInfo
    * is null in case of no room selected */
    private Long hospitalDepartmentRoomInfoId;

    /*id of assigned billing mode to hospital department -> HospitalDepartmentBillingModeInfo*/
    @NotNull
    private Long hospitalDepartmentBillingModeId;

    private Long patientId;

    @NotNull
    private Date appointmentDate;

    @NotNull
    @NotEmpty
    @NotBlank
    private String appointmentTime;

    @NotNull
    @NotEmpty
    @NotBlank
    private String userId;
}
