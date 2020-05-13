package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDROverrideDetailResponseDTO implements Serializable {

    private Long ddrOverrideId;

    private Date date;

    private Long shiftId;

    private String shiftName;

    private Date startTime;

    private Date endTime;

    private Integer rosterGapDuration;

    private Character offStatus;

    private String remarks;

    private Character hasBreak;
}
