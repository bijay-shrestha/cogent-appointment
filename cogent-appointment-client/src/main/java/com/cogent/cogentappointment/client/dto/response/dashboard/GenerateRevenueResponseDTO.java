package com.cogent.cogentappointment.client.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Getter
@Setter
public class GenerateRevenueResponseDTO implements Serializable {

    private String fiscalYear;

    private Double amount;

    private Double growthPercent;

    private Character filterType;
}
