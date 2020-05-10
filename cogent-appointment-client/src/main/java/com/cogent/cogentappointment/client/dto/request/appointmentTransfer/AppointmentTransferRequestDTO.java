package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/10/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferRequestDTO implements Serializable{

    private Long appointmentId;

    private Long specializationId;

    private Long doctorId;

    private Date appointmentDate;

    private String appointmentTime;

    private Double appointmentCharge;

    private String remarks;
}
