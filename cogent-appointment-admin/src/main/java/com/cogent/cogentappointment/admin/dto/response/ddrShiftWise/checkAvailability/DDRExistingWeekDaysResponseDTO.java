package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 13/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRExistingWeekDaysResponseDTO implements Serializable {

    private Long ddrWeekDaysId;

    private Date startTime;

    private Date endTime;

    private Character offStatus;

    private Character hasBreak;

    private Long weekDaysId;

    private String weekDaysName;
}
