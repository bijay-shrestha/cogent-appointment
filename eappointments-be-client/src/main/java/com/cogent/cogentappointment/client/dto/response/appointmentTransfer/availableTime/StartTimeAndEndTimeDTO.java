package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartTimeAndEndTimeDTO implements Serializable {

    private String startTime;

    private String endTime;


}
