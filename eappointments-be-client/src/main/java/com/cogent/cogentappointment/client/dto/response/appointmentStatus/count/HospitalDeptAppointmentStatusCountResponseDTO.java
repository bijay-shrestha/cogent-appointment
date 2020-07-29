package com.cogent.cogentappointment.client.dto.response.appointmentStatus.count;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Sauravi Thapa ON 6/28/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptAppointmentStatusCountResponseDTO implements Serializable {

    private Map<String, Long> appointmentStatusCount;
}
