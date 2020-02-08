package com.cogent.cogentappointment.admin.dto.response.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 07/02/2020
 */
@Getter
@Setter
public class AppointmentRefundResponseDTO implements Serializable {

    private Long appointmentId;

    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private String hospitalName;

    private String patientName;

    private String registrationNumber;

    private Character gender;

    private String age;

    private String doctorName;

    private String specializationName;

    private String eSewaId;

    private String transactionNumber;

    private Date cancelledDate;

    private Double refundAmount;

    private String cancellationRemarks;

    private Double totalAmount;

    private int totalItems;
}
