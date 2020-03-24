package com.cogent.cogentappointment.admin.dto.response.companyAdmin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 13/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminMetaInfoResponseDTO implements Serializable {

    private Long adminMetaInfoId;

    private String metaInfo;
}
