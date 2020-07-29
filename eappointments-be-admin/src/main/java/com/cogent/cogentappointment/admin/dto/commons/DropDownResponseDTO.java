package com.cogent.cogentappointment.admin.dto.commons;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DropDownResponseDTO implements Serializable {

    private Long value;

    private String label;
}
