package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class HospitalBillingModeUpdateRequestDTO implements Serializable {

    private Long billingModeId;

    @Status
    private Character status;
}
