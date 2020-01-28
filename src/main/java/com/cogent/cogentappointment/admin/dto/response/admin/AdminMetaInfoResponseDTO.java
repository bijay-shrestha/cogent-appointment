package com.cogent.cogentappointment.admin.dto.response.admin;

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
public class AdminMetaInfoResponseDTO implements Serializable {

    private Long adminMetaInfoId;

    private String metaInfo;
}
