package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 2019-10-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDetailResponseDTO implements Serializable {

    private Long patientTypeId;

    private String patientTypeName;

    private Long patientId;

    private String patientName;

    private Long appointmentTypeId;

    private String appointmentTypeName;

    private Long appointmentModeId;

    private String appointmentModeName;

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;

    private Long billTypeId;

    private String billTypeName;

    private Date appointmentDate;

    private Date startTime;

    private Date endTime;

    private String appointmentNumber;

    private Character status;

    private String reason;

    private Character emergency;

    private String referredBy;

    private String remarks;
}
