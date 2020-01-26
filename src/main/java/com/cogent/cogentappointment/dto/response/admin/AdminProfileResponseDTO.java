package com.cogent.cogentappointment.dto.response.admin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 26/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfileResponseDTO implements Serializable {
    private Long adminProfileId;

    private Long applicationModuleId;

    private String applicationModuleName;

    private Long profileId;

    private String profileName;
}
