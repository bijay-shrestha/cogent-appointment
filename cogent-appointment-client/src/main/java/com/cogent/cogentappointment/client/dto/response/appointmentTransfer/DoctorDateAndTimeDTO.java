package com.cogent.cogentappointment.client.dto.response.appointmentTransfer;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDateAndTimeDTO implements Serializable {

    private Date date;

    private List<String> time;


}
