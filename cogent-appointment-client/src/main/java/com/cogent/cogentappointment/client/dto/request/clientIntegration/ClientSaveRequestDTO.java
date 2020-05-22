package com.cogent.cogentappointment.client.dto.request.clientIntegration;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientSaveRequestDTO implements Serializable {

    private String name;

}
