package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDepartmentRevenueResponseDTO implements Serializable {

    private List<HospitalDepartmentRevenueDTO> revenueDTOS;

    private Long totalAppointmentCount;

    private Double totalRevenueAmount;

    private Long totalFollowUpCount;

    private int totalItems;

}
