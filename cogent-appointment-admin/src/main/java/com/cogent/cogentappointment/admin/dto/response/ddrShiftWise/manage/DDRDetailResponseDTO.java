package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDROverrideDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRShiftMinResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 14/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRDetailResponseDTO implements Serializable {

    private DDRResponseDTO ddrDetail;

    private List<DDRShiftMinResponseDTO> shiftDetail;

    private List<DDROverrideDetailResponseDTO> overrideDetail;

}
