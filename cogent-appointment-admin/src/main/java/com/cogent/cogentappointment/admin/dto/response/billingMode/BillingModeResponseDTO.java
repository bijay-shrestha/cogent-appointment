package com.cogent.cogentappointment.admin.dto.response.billingMode;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
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
public class BillingModeResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String code;

    private Character status;

    private String remarks;

    private String description;

}
