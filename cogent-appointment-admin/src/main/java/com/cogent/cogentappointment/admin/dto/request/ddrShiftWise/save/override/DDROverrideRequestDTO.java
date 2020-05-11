package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 11/05/20
 */
@Getter
@Setter
public class DDROverrideRequestDTO implements Serializable {

    private Character hasOverride;

    private Long ddrId;

    private List<DDROverrideDetailRequestDTO> overrideDetail;
}
