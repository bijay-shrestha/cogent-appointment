package com.cogent.cogentappointment.client.dto.response.specialization;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecializationResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String code;

    private Character status;

    private String remarks;
}
