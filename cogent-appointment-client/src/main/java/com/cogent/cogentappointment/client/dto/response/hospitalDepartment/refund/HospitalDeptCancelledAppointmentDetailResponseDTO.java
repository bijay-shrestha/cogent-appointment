package com.cogent.cogentappointment.client.dto.response.hospitalDepartment.refund;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 29/06/2020
 */
@Getter
@Setter
public class HospitalDeptCancelledAppointmentDetailResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private Long appointmentId;

    private Long appointmentModeId;

    private Long hospitalId;

    private String hospitalName;

    private String patientName;

    private String registrationNumber;

    private Gender gender;

    private String age;

    private String hospitalDepartmentName;

    private String roomNumber;

    private String eSewaId;

    private String transactionNumber;

    private String cancelledDate;

    private Double refundAmount;

    private String mobileNumber;

    private Double appointmentCharge;

    private String appointmentMode;

    private String remarks;

    private Character isRegistered;
}
