package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRExistingDetailResponseDTO implements Serializable {

    private Character hasOverride;

    private List<DDRShiftResponseDTO> shiftDetail;

    private List<DDROverrideDetailResponseDTO> overrideDetail;
}
