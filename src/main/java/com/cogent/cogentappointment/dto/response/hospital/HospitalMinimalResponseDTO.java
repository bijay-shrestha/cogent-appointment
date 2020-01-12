package com.cogent.cogentappointment.dto.response.hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rupak
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Character status;

    private String remarks;
}
