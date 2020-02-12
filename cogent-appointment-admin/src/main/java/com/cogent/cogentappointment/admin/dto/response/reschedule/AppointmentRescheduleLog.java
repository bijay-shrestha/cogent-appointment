package com.cogent.cogentappointment.admin.dto.response.reschedule;

import com.cogent.cogentappointment.persistence.enums.Gender;
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
public class AppointmentRescheduleLog {

    private String esewaId;

    private String hospitalName;

    private Date previousAppointmentDate;

    private String rescheduleAppointmentNumber;

    private String appointmentNumber;

    private String registrationNumber;

    private String patientName;

    private String patientAddress;

    private Gender patientGender;

    private String mobileNumber;

    private String doctorName;

    private String specializationName;

    private String transactionNumber;

    private Double amount;

    private String remarks;

}
