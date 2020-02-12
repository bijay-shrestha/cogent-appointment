package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@Getter
@Setter
public class GenerateRevenueResponseDTO implements Serializable {

    private Character revenueType;

    private Double amount;

    private Double growthPercent;
}
