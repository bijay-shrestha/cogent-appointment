package com.cogent.cogentappointment.client.dto.response.integration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/08-4:22 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestHeaderDetailResponse implements Serializable {

    private String keyParam;

    private String valueParam;

    private String description;
}
