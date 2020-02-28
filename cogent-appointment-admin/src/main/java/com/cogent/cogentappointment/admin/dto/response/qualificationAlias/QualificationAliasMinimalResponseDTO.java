package com.cogent.cogentappointment.admin.dto.response.qualificationAlias;

import lombok.*;

/**
 * @author Rupak
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QualificationAliasMinimalResponseDTO {

    private Long id;

    private String name;

    private Character status;

    private int totalItems;
}
