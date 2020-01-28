package com.cogent.cogentappointment.admin.dto.response.appointment;

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
public class AppointmentMinimalResponseDTO implements Serializable {

    private Long appointmentId;

    private String patientTypeName;

    private String patientName;

    private String appointmentTypeName;

    private String appointmentModeName;

    private String doctorName;

    private String billTypeName;

    private Date appointmentDate;

    private Date startTime;

    private Date endTime;

    private String appointmentNumber;

    private Character status;

    private int totalItems;
}
