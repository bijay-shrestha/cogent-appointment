package com.cogent.cogentappointment.admin.dto.response.doctor;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 08/05/20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorShiftMinResponseDTO implements Serializable {

    private Long value;

    private String label;

    /*isChecked = true (default) is sent in response in order to show checkboxes
    of assigned shifts as selected in the front-end. To bind/unbind selected check-boxes.*/
    private boolean isChecked = true;
}
