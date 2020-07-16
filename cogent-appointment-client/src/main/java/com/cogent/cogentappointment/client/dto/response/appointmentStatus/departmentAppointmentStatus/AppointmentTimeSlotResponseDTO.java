package com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/5/20
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
