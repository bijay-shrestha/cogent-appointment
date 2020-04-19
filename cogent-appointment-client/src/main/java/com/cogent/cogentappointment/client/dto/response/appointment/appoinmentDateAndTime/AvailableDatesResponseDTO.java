package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableDatesResponseDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY")
    private Date date;

    private String doctorAvailableTime;
}
