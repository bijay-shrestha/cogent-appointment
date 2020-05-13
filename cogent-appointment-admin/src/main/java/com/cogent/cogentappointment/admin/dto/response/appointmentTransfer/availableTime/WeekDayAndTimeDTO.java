package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekDayAndTimeDTO implements Serializable {

    private String startTime;

    private String endTime;


}
