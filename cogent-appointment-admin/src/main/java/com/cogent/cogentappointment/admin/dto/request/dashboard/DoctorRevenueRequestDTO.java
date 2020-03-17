package com.cogent.cogentappointment.admin.dto.request.dashboard;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rupak
 */
@Getter
@Setter
public class DoctorRevenueRequestDTO {

    private Long doctorId;

    private Long hospitalId;

    private Long specializationId;
}
