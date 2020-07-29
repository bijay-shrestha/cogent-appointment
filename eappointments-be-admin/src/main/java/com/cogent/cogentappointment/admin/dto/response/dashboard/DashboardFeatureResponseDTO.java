package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
public class DashboardFeatureResponseDTO implements Serializable {

    private Long id;

    private String code;

    private String name;
}
