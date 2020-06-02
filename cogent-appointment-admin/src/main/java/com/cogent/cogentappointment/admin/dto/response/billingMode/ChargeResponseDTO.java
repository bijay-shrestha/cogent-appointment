package com.cogent.cogentappointment.admin.dto.response.billingMode;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/2/20
 */
@Setter
@Getter
public class ChargeResponseDTO implements Serializable {

    private Double appointmentCharge;

    private Double followUpCharge;
}
