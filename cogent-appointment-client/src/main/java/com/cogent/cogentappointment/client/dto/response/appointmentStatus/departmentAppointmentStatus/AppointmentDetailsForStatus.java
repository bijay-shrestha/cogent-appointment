package com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Sauravi Thapa ON 6/17/20
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDetailsForStatus implements Serializable{

    private String appointmentTime;

    private String status;

    private Date appointmentDate;

    private String appointmentNumber;

    private String mobileNumber;

    private Gender gender;

    private String patientName;

    private Long appointmentId;

    private String age;

    private Character isFollowUp;

    private Character hasTransferred;

    private Long hospitalDepartmentId;

    private String hospitalDepartmentName;

    private Long hospitalDepartmentRoomInfoId;

    private String roomNumber;

}
