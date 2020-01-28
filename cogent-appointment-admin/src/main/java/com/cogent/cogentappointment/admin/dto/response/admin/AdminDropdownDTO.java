package com.cogent.cogentappointment.admin.dto.response.admin;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 2019-08-26
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDropdownDTO implements Serializable {

    private Long value;

    private String label;
}
