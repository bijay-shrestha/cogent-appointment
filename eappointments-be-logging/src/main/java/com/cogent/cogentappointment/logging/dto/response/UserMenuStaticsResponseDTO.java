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
public class UserMenuStaticsResponseDTO implements Serializable {

    private List<AdminLogStaticsResponseDTO> userMenuCountList;

    private Long totalCount;

    private int totalItems;

}
