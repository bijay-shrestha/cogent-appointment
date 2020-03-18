package com.cogent.cogentappointment.client.dto.response.appointment.refund;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 07/02/2020
 */
@Getter
@Setter
public class AppointmentRefundDTO implements Serializable {

    private Long appointmentId;

    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private String hospitalName;

    private String patientName;

    private String registrationNumber;

    private Gender gender;

    private String age;

    private String doctorName;

    private String specializationName;

    private String eSewaId;

    private String transactionNumber;

    private Date cancelledDate;

    private Double refundAmount;

    private String cancellationRemarks;

    private String mobileNumber;
}
