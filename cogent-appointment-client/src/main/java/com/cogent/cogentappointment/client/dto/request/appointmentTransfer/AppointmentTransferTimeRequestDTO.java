package com.cogent.cogentappointment.client.dto.request.appointmentTransfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/7/20
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTransferTimeRequestDTO implements Serializable{

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Kathmandu")
    private Date date;
}
