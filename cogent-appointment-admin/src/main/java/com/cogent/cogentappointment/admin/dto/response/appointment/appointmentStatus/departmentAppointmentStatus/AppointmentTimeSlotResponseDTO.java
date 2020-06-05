package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 17/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTimeSlotResponseDTO implements Serializable {

    private String appointmentTime;

    private String status;

    private String appointmentNumber;

    private String mobileNumber;

    private String age;

    private String gender;

    private String patientName;

    private Long appointmentId;

    private boolean hasTimePassed;

    private Character isFollowUp;

    private Character hasTransferred;
}
