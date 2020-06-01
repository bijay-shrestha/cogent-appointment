package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDepartmentMinimalResponse implements Serializable {

    private BigInteger id;

    private String hospitalName;

    private String name;

    private Character status;

    private String roomList;

    private List<BillingModeChargeResponse> billingModeChargeResponseList;
}
