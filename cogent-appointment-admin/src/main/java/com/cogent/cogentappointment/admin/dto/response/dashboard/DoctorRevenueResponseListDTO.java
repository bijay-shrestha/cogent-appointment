package com.cogent.cogentappointment.admin.dto.response.dashboard;

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

    private Long totalAppointmentCount;

    private Double totalRevenueAmount;
}
