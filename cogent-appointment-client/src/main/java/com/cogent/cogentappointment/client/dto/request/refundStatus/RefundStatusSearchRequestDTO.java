package com.cogent.cogentappointment.client.dto.request.refundStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class RefundStatusSearchRequestDTO implements Serializable{


    private Date fromDate;

    private Date toDate;

    private Long patientMetaInfoId;

    private Long doctorId;

    private Long specializationId;

    private String appointmentNumber;

    /*NEW OR REGISTERED*/
    private Character patientType;

    private String esewaId;

    private String esewaMerchantCode;

    private String transactionNumber;
}
