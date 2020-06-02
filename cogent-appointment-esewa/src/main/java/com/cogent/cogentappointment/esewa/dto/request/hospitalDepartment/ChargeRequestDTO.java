package com.cogent.cogentappointment.esewa.dto.request.hospitalDepartment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/2/20
 */
@Setter
@Getter
public class ChargeRequestDTO implements Serializable {

    @NotNull(message = "Billing Mode cannot be Null")
    private Long billingModeId;

    @NotNull(message = "Hospital Department cannot be Null")
    private Long hospitalDepartmentId;
}
