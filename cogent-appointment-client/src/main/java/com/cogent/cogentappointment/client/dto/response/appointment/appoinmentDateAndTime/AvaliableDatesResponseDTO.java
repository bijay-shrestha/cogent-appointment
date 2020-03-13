package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliableDatesResponseDTO implements Serializable {

    private Date date;

    private String doctorAvailableTime;
}
