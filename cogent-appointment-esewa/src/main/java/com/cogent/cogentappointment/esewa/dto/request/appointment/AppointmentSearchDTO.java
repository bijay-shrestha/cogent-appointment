package com.cogent.cogentappointment.esewa.dto.request.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 06/02/2020
 */
@Getter
@Setter
public class AppointmentSearchDTO implements Serializable {

    private Date fromDate;

    private Date toDate;
}
