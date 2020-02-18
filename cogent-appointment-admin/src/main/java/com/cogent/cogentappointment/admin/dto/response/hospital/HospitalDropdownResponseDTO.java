package com.cogent.cogentappointment.admin.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti ON 25/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDropdownResponseDTO implements Serializable {

    private Long value;

    private String label;

    private Character isCogentAdmin;
}
