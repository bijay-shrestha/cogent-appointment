package com.cogent.cogentappointment.admin.dto.response.billingMode;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/29/2020
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingModeMinimalResponseDTO implements Serializable {

   private List<BillingModeMinimalResponse> billingModeList;

    private int totalItems;
}
