package com.cogent.cogentappointment.admin.dto.response.companyAdmin;

import com.cogent.cogentappointment.admin.dto.response.admin.AdminMacAddressInfoResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-08-29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminDetailResponseDTO implements Serializable {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private Character status;

    private Character hasMacBinding;

    private Gender gender;

    private Long companyId;

    private String companyName;

    private Long profileId;

    private String profileName;

    private String fileUri;

    private String remarks;

    private List<AdminMacAddressInfoResponseDTO> adminMacAddressInfo;
}
