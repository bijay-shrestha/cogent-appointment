package com.cogent.cogentappointment.esewa.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Getter
@Setter
public class RevenueStatisticsResponseDTO implements Serializable {

    private String fiscalYear;

    private Double amount;

    private Double growthPercent;

    private Character filterType;
}
