package com.cogent.cogentappointment.admin.dto.request.dashboard;

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

    private Long hospitalId;

    private Date fromDate;

    private Date toDate;
}
