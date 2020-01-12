package com.cogent.cogentappointment.dto.response.hospital;

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
public class HospitalContactNumberResponseDTO implements Serializable {

    private Long hospitalContactNumberId;

    private String contactNumber;
}
