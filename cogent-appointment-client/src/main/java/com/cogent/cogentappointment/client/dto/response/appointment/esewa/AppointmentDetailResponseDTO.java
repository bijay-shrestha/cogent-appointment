package com.cogent.cogentappointment.client.dto.response.appointment.esewa;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 17/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDetailResponseDTO implements Serializable {

    private String hospitalName;

    private String patientName;

    private String doctorName;

    private String doctorSalutation;

    private String specializationName;

    private String appointmentNumber;

    private Date appointmentDate;

    private String appointmentTime;

    private String mobileNumber;

    private Date dateOfBirth;

    private Double appointmentAmount;

    private Double taxAmount;

    private Double serviceChargeAmount;

    private Double discountAmount;

}
