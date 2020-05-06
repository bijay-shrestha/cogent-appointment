package com.cogent.cogentappointment.client.dto.response.appointmentTransfer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Getter
@Setter
@Builder
public class DoctorDatesResponseDTO implements Serializable {

    private Long id;

    private Date fromDate;

    private Date toDate;

}
