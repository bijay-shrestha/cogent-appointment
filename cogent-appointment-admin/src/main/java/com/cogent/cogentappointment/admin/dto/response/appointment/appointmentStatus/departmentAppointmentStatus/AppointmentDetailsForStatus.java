package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Sauravi Thapa ON 6/17/20
 */

@Setter
@Getter
@Builder
public class AppointmentDetailsForStatus implements Serializable{

    private String appointmentTime;

    private Character status;

    private Date appointmentDate;

    private String appointmentNumber;

    private String mobileNumber;

    private Gender gender;

    private String patientName;

    private Long appointmentId;

    private Character isFollowUp;

    private Character hasTransferred;

    private Long hospitalDepartmentid;

    private String hospitalDepartmentName;

    private Long hospitalDepartmentRoomInfoId;

    private String roomNumber;




}
