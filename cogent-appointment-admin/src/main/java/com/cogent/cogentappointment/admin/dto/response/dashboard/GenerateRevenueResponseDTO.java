package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Getter
@Setter
public class GenerateRevenueResponseDTO implements Serializable {

    private Double amount;

    private Double growthPercent;
}
