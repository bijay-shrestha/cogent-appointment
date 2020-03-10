package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRevenueResponseDTO implements Serializable {

    private Long doctorId;

    private String doctorName;

    private String fileUri;

    private String speciliazation;

    private Long totalAppointmentCount;

    private Double revenueAmount;

    private int totalItems;
}
