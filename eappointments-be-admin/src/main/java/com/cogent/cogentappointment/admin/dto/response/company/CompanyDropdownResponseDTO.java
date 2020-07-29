package com.cogent.cogentappointment.admin.dto.response.company;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 25/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDropdownResponseDTO implements Serializable {

    private Long value;

    private String label;

    private Character isCompany;

    private String companyCode;
}
