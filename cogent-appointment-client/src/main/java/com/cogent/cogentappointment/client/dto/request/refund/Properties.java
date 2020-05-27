package com.cogent.cogentappointment.client.dto.request.refund;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Properties implements Serializable {

    private String hospitalName;

    private Long appointmentId;
}
