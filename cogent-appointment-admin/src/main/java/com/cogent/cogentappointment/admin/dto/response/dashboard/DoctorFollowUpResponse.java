package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/28/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorFollowUpResponse implements Serializable {
    private Long followUpCount;

    private Double followUpAmount;
}
