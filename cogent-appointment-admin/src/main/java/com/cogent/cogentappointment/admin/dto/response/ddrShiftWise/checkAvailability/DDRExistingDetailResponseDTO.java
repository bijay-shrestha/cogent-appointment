package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDRExistingDetailResponseDTO implements Serializable {

    private List<DDRShiftResponseDTO> shiftDetail;

    private List<DDROverrideDetailResponseDTO> overrideDetail;
}
