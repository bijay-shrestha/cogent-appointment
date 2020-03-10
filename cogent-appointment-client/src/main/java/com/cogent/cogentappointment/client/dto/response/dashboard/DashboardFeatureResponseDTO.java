package com.cogent.cogentappointment.client.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
public class DashboardFeatureResponseDTO implements Serializable {

    private String code;

    private String name;

    private Character status;
}
