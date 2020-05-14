package com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverrideDatesResponseDTO implements Serializable {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private Character dayOffStatus;

}
