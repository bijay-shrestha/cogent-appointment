package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 14/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRBreakResponseDTO implements Serializable{

    private Long ddrBreakId;

    private Long breakTypeId;

    private String breakTypeName;

    private Date startTime;

    private Date endTime;

    private String remarks;
}
