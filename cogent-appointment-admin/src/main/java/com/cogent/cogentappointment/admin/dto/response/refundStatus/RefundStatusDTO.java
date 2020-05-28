package com.cogent.cogentappointment.admin.dto.response.refundStatus;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
@Getter
@Setter
public class RefundStatusDTO implements Serializable {

    private Long id;

    private Long appointmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private String patientName;

    private String registrationNumber;

    private Gender gender;

    private String age;

    private String doctorName;

    private String fileUri;

    private String specializationName;

    private String eSewaId;

    private String transactionNumber;

    private String cancelledDate;

    private String cancelledTime;

    private Double refundAmount;

    private String mobileNumber;

    private String appointmentMode;

    private Character isRegistered;

    private String refundStatus;

    private String remarks;

    private String hospitalName;
}
