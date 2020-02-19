package com.cogent.cogentappointment.client.dto.response.weekdays;

import lombok.*;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekDaysResponseDTO {

    private Long weekDaysId;

    private String weekDaysName;

    private String startTime = "";

    private String endTime = "";

    private Character dayOffStatus = 'N';

    private String errorMessage = "";

}
