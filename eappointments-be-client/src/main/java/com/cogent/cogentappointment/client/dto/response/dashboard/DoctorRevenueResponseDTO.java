package com.cogent.cogentappointment.client.dto.response.dashboard;

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

    private Long totalFollowUpCount;

    private Double totalRevenueAmount;

    private int totalItems;
}
