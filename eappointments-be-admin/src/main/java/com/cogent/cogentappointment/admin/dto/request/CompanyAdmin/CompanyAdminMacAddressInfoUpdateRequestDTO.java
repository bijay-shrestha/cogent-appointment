package com.cogent.cogentappointment.admin.dto.request.CompanyAdmin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-01
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminMacAddressInfoUpdateRequestDTO implements Serializable {

    private Long id;

    private String macAddress;

    private Character status;
}
