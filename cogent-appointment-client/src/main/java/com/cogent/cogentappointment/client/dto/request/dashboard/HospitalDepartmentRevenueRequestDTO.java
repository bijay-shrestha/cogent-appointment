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
public class HospitalDepartmentRevenueRequestDTO implements Serializable {

    private Long hospitalDepartmentId;

    private Date fromDate;

    private Date toDate;
}
