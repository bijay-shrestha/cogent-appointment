package com.cogent.cogentappointment.client.dto.response.eSewa;

import lombok.*;
import org.springframework.http.HttpStatus;

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
public class AllAvailableDatesResponseDTO implements Serializable {
    private List<Date> avaliableDates;

    private int responseCode;

    private HttpStatus responseStatus;
}
