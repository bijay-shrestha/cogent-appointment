package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author smriti on 24/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusCountResponseDTO implements Serializable {

    /*eg <V, 10> -> status, count*/
    private HashMap<String, Integer> vacantCount;

    private HashMap<String, Integer> bookedCount;

    private HashMap<String, Integer> checkedInCount;

    private HashMap<String, Integer> cancelledCount;

    private HashMap<String, Integer> followUpCount;

    private HashMap<String, Integer> allCount;
}
