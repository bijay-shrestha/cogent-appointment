package com.cogent.cogentappointment.admin.dto.request.dashboard;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRevenueRequestDTO implements Serializable {

    @NotNull
    private Date toDate;

    @NotNull
    private Date fromDate;

    @NotNull
    private Long hospitalId;
}
