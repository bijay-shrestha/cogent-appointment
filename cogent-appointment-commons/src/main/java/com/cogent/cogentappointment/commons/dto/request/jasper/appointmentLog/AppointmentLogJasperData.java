package com.cogent.cogentappointment.commons.dto.request.jasper.appointmentLog;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/10-4:06 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentLogJasperData implements Serializable {


    private String appointmentStatus;

    private String appointmentNumber;

    private String appointmentDateTime;

    private String appointmentTransactionDate;

    private String transactionDetails;

    private String patientDetails;

    private String esewaId;

    private String registrationNumber;

    private String address;

    private String doctorDetails;




}
