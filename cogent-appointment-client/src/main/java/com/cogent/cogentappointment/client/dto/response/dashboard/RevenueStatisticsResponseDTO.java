package com.cogent.cogentappointment.client.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Sauravi Thapa २०/२/११
 */
@Getter
@Setter
public class RevenueStatisticsResponseDTO implements Serializable {
    private Map<String, String> data;
    private Character filterType;
}
