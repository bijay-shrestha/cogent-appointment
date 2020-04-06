package com.cogent.cogentappointment.client.dto.request.dashboard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class DashboardFeatureRequestDTO implements Serializable {

    private Long adminId;

    private Long hospitalId;

}
