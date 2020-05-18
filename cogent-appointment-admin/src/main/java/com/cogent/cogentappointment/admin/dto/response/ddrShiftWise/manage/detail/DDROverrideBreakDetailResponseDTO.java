package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
public class DDROverrideBreakDetailResponseDTO implements Serializable {

    private Long ddrOverrideBreakId;

    private Long breakTypeId;

    private String breakTypeName;

    private Date startTime;

    private Date endTime;

    private String remarks;
}
