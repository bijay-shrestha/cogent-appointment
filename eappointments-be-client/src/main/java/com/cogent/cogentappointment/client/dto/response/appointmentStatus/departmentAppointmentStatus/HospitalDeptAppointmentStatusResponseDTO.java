package com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptAppointmentStatusResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd,YYYY",timezone = "Asia/Kathmandu")
    private LocalDate date;

    private Long departmentId;

    private Long hospitalDepartmentRoomInfoId;

    private String appointmentTimeDetails;

    private String appointmentNumber;

    private String mobileNumber;

    private String age;

    private String gender;

    private String patientName;

    private Long appointmentId;

    private Character isFollowUp;

    private Character hasTransferred;
}
