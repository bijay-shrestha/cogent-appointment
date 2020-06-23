package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDepartmentRevenueDTO implements Serializable {

    private Long hospitalDepartmentId;

    private String hospitalDepartmentName;

    private Long successfulAppointments = 0L;

    private Long cancelledAppointments = 0L;

    private Long totalAppointments = 0L;

    private Long totalFollowUp = 0L;

    private Double departmentRevenue = 0D;

    private Double cancelledRevenue = 0D;

    private Double totalRevenue = 0D;
}
