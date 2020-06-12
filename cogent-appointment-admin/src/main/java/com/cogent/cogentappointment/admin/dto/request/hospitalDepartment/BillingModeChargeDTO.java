package com.cogent.cogentappointment.admin.dto.request.hospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/1/20
 */
@Setter
@Getter
public class BillingModeChargeDTO implements Serializable {

    @NotNull(message = "Billing Mode cannot be null")
    private Long billingModeId;

    @NotNull
    private Double appointmentCharge;

    @NotNull
    private Double followUpCharge;

}
