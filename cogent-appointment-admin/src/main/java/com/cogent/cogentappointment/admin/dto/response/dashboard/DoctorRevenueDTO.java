package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRevenueDTO implements Serializable {

    private Long doctorId;

    private String doctorName;

    private Long specializationId;

    private String specializationName;

    private String fileUri;

    private Long successfulAppointment;

    private Long cancelledAppointment;

    private Double doctorRevenue;

    private Double companyRevenue;
}
