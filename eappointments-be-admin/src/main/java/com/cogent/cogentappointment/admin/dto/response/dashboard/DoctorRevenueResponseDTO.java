package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRevenueResponseDTO implements Serializable {

    private List<DoctorRevenueDTO> doctorRevenueInfo;

    private Long totalAppointmentCount;

    private Double totalRevenueAmount;

    private Long totalFollowUpCount;

    private int totalItems;

}
