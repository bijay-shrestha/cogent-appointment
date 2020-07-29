package com.cogent.cogentappointment.client.dto.response.hospitalDepartment.refund;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/22-3:29 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelledHospitalDeptAppointmentResponseDTO implements Serializable{

    private List<CancelledHospitalDeptAppointmentDTO> cancelledAppointments;

    private int totalItems;

    private Double totalRefundAmount;
}
