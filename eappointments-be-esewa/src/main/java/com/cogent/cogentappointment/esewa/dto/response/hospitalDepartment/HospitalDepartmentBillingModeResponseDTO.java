package com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 08/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDepartmentBillingModeResponseDTO implements Serializable {

    private Long hospitalDepartmentBillingModeId;

    private String billingModeName;
}


