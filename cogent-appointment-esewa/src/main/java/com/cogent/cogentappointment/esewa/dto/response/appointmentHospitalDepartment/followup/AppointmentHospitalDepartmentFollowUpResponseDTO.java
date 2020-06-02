package com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.followup;

import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 02/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentHospitalDepartmentFollowUpResponseDTO extends StatusResponseDTO implements Serializable {

    private Character isFollowUp;

    private Double appointmentCharge;

    private Long parentAppointmentId;

    private Long appointmentReservationId;

    private Double refundPercentage;
}
