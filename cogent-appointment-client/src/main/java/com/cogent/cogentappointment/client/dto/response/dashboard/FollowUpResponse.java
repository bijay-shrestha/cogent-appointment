package com.cogent.cogentappointment.client.dto.response.dashboard;

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
public class FollowUpResponse implements Serializable {
    private Long count;

    private Double amount;
}
