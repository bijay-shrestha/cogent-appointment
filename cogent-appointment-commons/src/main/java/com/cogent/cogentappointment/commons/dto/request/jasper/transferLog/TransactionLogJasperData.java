package com.cogent.cogentappointment.commons.dto.request.jasper.transferLog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/09-2:36 PM
 */
@Getter
@Setter
public class TransactionLogJasperData implements Serializable {

    private String serialNumber;
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
