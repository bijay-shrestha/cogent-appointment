package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-21
 */
@Getter
@Setter
public class AdminModeFeatureIntegrationResponseDTO implements Serializable {

    private Long appointmentModeId;

    private List<FeatureIntegrationResponseDTO> features;

}
