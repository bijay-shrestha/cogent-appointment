package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRWeekDaysResponseDTO implements Serializable {

    private Long ddrWeekDaysId;

    private Date startTime;

    private Date endTime;

    private Character offStatus;

    private Character hasBreak;

    private Long weekDaysId;

    private String weekDaysName;

    private List<DDRBreakResponseDTO> breakDetail;
}
