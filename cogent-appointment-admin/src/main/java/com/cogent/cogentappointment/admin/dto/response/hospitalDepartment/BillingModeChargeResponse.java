package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/1/20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingModeChargeResponse implements Serializable {

    private Long id;

    private Long billingModeId;

    private String billingMode;

    private Double appointmentCharge;

    private Double followUpCharge;
}
