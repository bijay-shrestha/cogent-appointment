package com.cogent.cogentappointment.admin.dto.response.company;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyContactNumberResponseDTO implements Serializable {

    private Long hospitalContactNumberId;

    private String contactNumber;

    private Character status;
}
