package com.cogent.cogentappointment.client.dto.request.integrationClient;

import lombok.*;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsewaPayementStatus {

    private String transaction_code;
    private String product_code;
    private String esewa_id;
}
