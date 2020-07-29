package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/28/20
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCountWithStatusDTO implements Serializable{

    private Long count;

    private String status;
}
