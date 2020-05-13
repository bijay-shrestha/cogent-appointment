package com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 12/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DDRExistingMinResponseDTO implements Serializable {

    private Boolean hasExistingRosters;

   private List<DDRExistingMinDTO> existingRosters;
}
