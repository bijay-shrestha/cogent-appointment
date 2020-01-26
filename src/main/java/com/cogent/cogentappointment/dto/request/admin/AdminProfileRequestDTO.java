package com.cogent.cogentappointment.dto.request.admin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 14/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProfileRequestDTO implements Serializable {

    private Long applicationModuleId;

    private Long profileId;
}
