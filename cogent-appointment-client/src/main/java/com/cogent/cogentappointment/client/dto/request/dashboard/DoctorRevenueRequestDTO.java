package com.cogent.cogentappointment.client.dto.request.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRevenueRequestDTO implements Serializable {

    private Date toDate;

    private Date fromDate;

    private Long hospitalId;
}
