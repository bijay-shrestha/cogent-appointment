package com.cogent.cogentappointment.esewa.dto.request.reschedule;

import lombok.*;

import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRescheduleDTO {

    private Long hospitalId;

    private Date previousAppointmentDate;

    private Date rescheduleAppointmentDate;

    private String registrationNumber;

    private Long appointmentId;

    private Long patientId;

    private Long doctorId;

    private Long patientMetaInfoId;

    private String transactionNumber;

    private String amount;

    private String remarks;

}
