package com.cogent.cogentappointment.client.dto.response.qualificationAlias;

import lombok.*;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QualificationAliasMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Character status;

    private int totalItems;
}
