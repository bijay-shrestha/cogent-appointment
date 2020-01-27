package com.cogent.cogentappointment.dto.response.admin;

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

    private String username;

    private String mobileNumber;

    private String email;

    private Character status;

    private Character hasMacBinding;

    private String fileUri;

    private String remarks;

    private Long hospitalId;

    private String hospitalName;

    private List<AdminProfileResponseDTO> adminProfileResponseDTOS;

    private List<MacAddressInfoResponseDTO> macAddressInfoResponseDTOS;
}
