package com.cogent.cogentappointment.client.dto.response.appointmentServiceType;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/25-2:35 PM
 */
@Getter
@Setter
public class PrimaryAppointmentServiceTypeResponse implements Serializable {

    private Long value;

    private String name;
}
