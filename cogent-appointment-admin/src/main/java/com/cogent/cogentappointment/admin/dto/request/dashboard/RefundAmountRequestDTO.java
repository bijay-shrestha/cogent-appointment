package com.cogent.cogentappointment.admin.dto.request.dashboard;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundAmountRequestDTO implements Serializable {

    Date fromDate;

    Date toDate;

    Long doctorId;

    Long hospitalId;

    Long specializationId;
}
