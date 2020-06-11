package com.cogent.cogentappointment.admin.dto.response.integration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/08-4:23 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiQueryParametersDetailResponse implements Serializable{

    private String keyParam;

    private String valueParam;

    private String description;

}
