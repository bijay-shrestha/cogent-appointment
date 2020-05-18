package com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 17/05/20
 */
@Getter
@Setter
public class DDROverrideBreakUpdateRequestDTO implements Serializable {

    private Long ddrBreakId;

    private Long breakTypeId;

    private Date startTime;

    private Date endTime;

    private Character status;

    private String remarks;

    private Boolean isBreakUpdated;
}
