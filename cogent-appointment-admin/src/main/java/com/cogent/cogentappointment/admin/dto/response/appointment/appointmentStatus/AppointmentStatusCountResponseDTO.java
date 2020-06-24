package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 24/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusCountResponseDTO implements Serializable {

    private Integer vacantCount;

    private Integer bookedCount;

    private Integer checkedInCount;

    private Integer cancelledCount;

    private Integer followUpCount;
}
