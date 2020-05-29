package com.cogent.cogentappointment.admin.dto.request.billingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/29/2020
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingModeSearchRequestDTO implements Serializable {

    private Long id;

    private String code;

    private Character status;
}
