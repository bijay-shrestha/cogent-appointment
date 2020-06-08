package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 08/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDepartmentResponseDTO implements Serializable {

    private Long hospitalDepartmentId;

    private String hospitalDepartmentName;

    private List<HospitalDepartmentBillingModeResponseDTO> billingModeInfo;
}
