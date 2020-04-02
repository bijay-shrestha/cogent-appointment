package com.cogent.cogentappointment.esewa.dto.request.reschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleLogSearchDTO implements Serializable{

    private Long doctorId;

    private Date fromDate;

    private Date toDate;

    private Long patientMetaInfoId;

    private String appointmentNumber;

    private Long appointmentId;

    private Long esewaId;

    private Long specializationId;

    private Character patientType;
}
