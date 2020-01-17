package com.cogent.cogentappointment.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 09/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTimeResponseDTO implements Serializable {

    private Date startTime;

    private Date endTime;
}
