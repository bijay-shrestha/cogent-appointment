package com.cogent.cogentappointment.logging.dto.response;

import lombok.*;

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
public class UserMenuLogResponseDTO implements Serializable {

    private List<UserMenuLogSearchResponseDTO> userLogList;

    private int totalItems;

}
