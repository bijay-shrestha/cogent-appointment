package com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime;

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
public class ActualDateAndTimeResponseDTO implements Serializable {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private Integer gapDuration;

    private Character hasOverride;

}
