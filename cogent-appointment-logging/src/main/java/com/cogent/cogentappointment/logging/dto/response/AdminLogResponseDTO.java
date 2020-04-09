package com.cogent.cogentappointment.logging.dto.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminLogResponseDTO implements Serializable {

    private List<AdminLogSearchResponseDTO> userLogList;

    private int totalItems;

}
