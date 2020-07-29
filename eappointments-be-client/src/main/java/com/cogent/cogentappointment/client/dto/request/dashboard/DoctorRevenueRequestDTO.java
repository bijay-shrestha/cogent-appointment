package com.cogent.cogentappointment.client.dto.request.dashboard;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRevenueRequestDTO implements Serializable {

    private Long doctorId;

    private Long hospitalId;

    private Long specializationId;

    private Date fromDate;

    private Date toDate;
}
