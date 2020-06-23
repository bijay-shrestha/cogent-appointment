package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rupak ON 2020/06/22-3:31 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCancelApprovalResponseDTO implements Serializable {

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

    private String eSewaId;

    private String transactionNumber;

    private String cancelledDate;

    private String mobileNumber;

    private Double refundAmount;

    private String appointmentMode;

    private Character isRegistered;

    private String departmentName;

    private String roomNumber;
}
