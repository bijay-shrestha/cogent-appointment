package com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment;

import com.cogent.cogentappointment.persistence.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDepartmentCheckInDetailResponseDTO implements Serializable {

    private Long appointmentId;

    private Long hospitalId;

    private String hospitalName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY", timezone = "Asia/Kathmandu")
    private Date appointmentDate;

    private String appointmentTime;

    private String appointmentNumber;

    private String appointmentMode;

    private Double appointmentAmount;

    private String patientName;

    private String mobileNumber;

    private Gender gender;

    private Integer age;

    private Integer ageMonth;

    private Integer ageDay;

    private String eSewaId;

    private Character isRegistered;

    private String registrationNumber;

    private String hospitalNumber;

    private String hospitalDepartmentName;

    private String roomNumber;

    private String billingModeName;

    private String address;

    private String province;

    private String district;

    private String vdcOrMunicipality;

    private String wardNumber;

    private String transactionNumber;

    private Character isSelf;

    private String emailAddress;

}
