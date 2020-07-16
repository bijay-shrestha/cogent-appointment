package com.cogent.cogentappointment.admin.dto.request.reschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogSearchDTO {

    private Long hospitalId;

    private Long doctorId;

    private Date fromDate;

    private Date toDate;

    private Long patientMetaInfoId;

    private String appointmentNumber;

    private Long appointmentId;

    private Long esewaId;

    private Long specializationId;

    private Character patientType;

    @NotNull
    private String appointmentServiceTypeCode;

    private Long hospitalDepartmentId;
}
