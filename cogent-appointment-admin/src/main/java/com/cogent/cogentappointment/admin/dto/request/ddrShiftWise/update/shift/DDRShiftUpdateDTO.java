package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.shift;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 17/05/20
 */
@Getter
@Setter
public class DDRShiftUpdateDTO implements Serializable {

    private Long ddrShiftDetailId;

    private Long shiftId;

    private Integer rosterGapDuration;

    private Character status;
}
