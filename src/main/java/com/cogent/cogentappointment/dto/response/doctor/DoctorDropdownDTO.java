package com.cogent.cogentappointment.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDropdownDTO implements Serializable {

    private Long value;

    private String label;

    private String fileUri;
}
