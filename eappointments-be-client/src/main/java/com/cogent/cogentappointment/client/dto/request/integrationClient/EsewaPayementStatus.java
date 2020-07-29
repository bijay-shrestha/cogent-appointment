package com.cogent.cogentappointment.client.dto.request.integrationClient;

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
public class EsewaPayementStatus implements Serializable{

    private String transaction_code;

    private String product_code;

    private String esewa_id;
}
