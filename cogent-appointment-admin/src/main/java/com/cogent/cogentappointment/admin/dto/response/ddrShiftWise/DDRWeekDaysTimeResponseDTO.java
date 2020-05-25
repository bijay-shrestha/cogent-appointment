package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 19/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRWeekDaysTimeResponseDTO implements Serializable {

    private Date startTime;

    private Date endTime;

    private String shiftName;

    private Long weekDaysId;

    private String weekDaysName;
}
