package com.cogent.cogentappointment.admin.dto.response.admin;

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
public class AdminDetailResponseDTO implements Serializable {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private Character status;

    private Character hasMacBinding;

    private Gender gender;

    private Long hospitalId;

    private String hospitalName;

    private Long profileId;

    private String profileName;

    private Long departmentId;

    private String departmentName;

    private String fileUri;

    private String remarks;

    private String hospitalAlias;

    private List<AdminMacAddressInfoResponseDTO> adminMacAddressInfo;
}
