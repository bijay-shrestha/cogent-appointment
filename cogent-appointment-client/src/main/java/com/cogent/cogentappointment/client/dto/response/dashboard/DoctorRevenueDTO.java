package com.cogent.cogentappointment.client.dto.response.dashboard;

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

    private Long successfulAppointments = 0L;

    private Long cancelledAppointments = 0L;

    private Long totalAppointments = 0L;

    private Long totalFollowUp = 0L;

    private Double doctorRevenue = 0D;

    private Double cancelledRevenue = 0D;

    private Double totalRevenue = 0D;
}
