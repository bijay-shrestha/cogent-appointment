package com.cogent.cogentappointment.esewa.dto.response.dashboard;

import lombok.*;

import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRevenueResponseListDTO {

    private List<DoctorRevenueResponseDTO> doctorRevenueResponseDTOList;

    private Long overallAppointmentCount;

    private int totalItems;

    private Double totalRevenueAmount;
}
