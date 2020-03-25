package com.cogent.cogentappointment.admin.dto.response.companyProfile;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 7/10/19
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyProfileMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Character status;

    private String companyName;

    private Integer totalItems;
}
