package com.cogent.cogentappointment.client.dto.response.appointmentMode;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 11/11/2019
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentModeResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Character status;

    private String description;

    private String createdBy;

    private String modifiedBy;

    private Date createdDate;

    private Date modifiedDate;

}
