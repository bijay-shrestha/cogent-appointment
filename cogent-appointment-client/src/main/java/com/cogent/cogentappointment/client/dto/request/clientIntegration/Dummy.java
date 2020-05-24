package com.cogent.cogentappointment.client.dto.request.clientIntegration;

import lombok.*;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dummy {

    private String uuid;
    private String module;
    private String device_unique_id;
}
