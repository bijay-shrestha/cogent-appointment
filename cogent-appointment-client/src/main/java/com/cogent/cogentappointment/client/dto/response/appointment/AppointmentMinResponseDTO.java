package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 06/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentMinResponseDTO implements Serializable {

    private Long appointmentId;

    private String patientName;

    private String doctorName;

    private String hospitalName;

    private String specializationName;

    private String appointmentNumber;

    private Date appointmentDate;

    private String appointmentTime;

    private Double appointmentAmount;
}
