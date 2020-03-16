package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateBySpecializationIdResponseDTO implements Serializable {
    private Long doctorId;

    private String doctorName;

    private List<Date> avaliableDates;
}
