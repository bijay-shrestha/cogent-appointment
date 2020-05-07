package com.cogent.cogentappointment.client.dto.response.appointment.log;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 28/04/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedAppointmentResponseDTO implements Serializable {

    private Long bookedCount;

    private Double bookedAmount;

    private Long followUpCount;

    private Double followUpAmount;
}
