package com.cogent.cogentappointment.admin.dto.response.weekDays;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 02/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekDaysMinResponseDTO implements Serializable {

    private Long weekDaysId;

    private String weekDaysName;
}
