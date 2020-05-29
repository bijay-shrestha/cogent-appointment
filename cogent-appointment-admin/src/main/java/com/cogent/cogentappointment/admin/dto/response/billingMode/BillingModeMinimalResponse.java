package com.cogent.cogentappointment.admin.dto.response.billingMode;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/29/2020
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingModeMinimalResponse implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Character status;

    private int totalItems;
}
