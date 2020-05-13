package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDROverrideBreakResponseDTO implements Serializable {

    private Long breakTypeId;

    private String breakTypeName;

    private String startTime;

    private String endTime;

    private Character status;

    private String remarks;
}
