package com.cogent.cogentappointment.admin.dto.response.clientIntegration;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientApiIntegrationSearchDTO implements Serializable {

    private List<ClientApiIntegrationSearchResponseDTO> searchResponseDTOS;

    private int totalItems;
}
