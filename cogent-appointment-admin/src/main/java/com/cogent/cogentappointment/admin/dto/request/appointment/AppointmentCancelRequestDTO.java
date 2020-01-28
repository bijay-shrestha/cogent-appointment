package com.cogent.cogentappointment.admin.dto.request.appointment;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * @author smriti on 2019-10-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCancelRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotNull
    private Character status;
}
