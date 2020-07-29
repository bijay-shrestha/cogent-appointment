package com.cogent.cogentappointment.admin.dto.response.integrationAdminMode;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/03-9:46 AM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeIntegrationSearchDTO implements Serializable {

    private List<AdminModeIntegrationSearchResponseDTO> searchResponseDTOS;

    private int totalItems;

}
