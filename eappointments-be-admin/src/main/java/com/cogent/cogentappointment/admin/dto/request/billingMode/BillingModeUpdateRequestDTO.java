package com.cogent.cogentappointment.admin.dto.request.billingMode;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/29/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingModeUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String code;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;
}
