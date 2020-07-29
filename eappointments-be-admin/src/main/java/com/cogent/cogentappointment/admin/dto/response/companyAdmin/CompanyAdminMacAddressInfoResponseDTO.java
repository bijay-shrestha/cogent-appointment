package com.cogent.cogentappointment.admin.dto.response.companyAdmin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-08-29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminMacAddressInfoResponseDTO implements Serializable {

    private Long id;

    private String macAddress;
}
