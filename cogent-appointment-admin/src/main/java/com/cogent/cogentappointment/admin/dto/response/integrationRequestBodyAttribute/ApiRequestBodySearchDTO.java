package com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/05/31-9:15 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestBodySearchDTO implements Serializable{

    private int totalItems;

    private List<ApiIntegrationRequestBodySearchResponseDTO> searchResponseDTOList;
}
