package com.cogent.cogentappointment.client.dto.response.refundStatus;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDepartmentRefundStatusResponseDTO implements Serializable {

    private Double totalRefundAmount;

    private int totalItems;

    private List<HospitalDepartmentRefundStatusDTO> refundAppointments;
}
