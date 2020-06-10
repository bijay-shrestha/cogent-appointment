package com.cogent.cogentappointment.client.dto.request.refund;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsewaRefundRequestDTO implements Serializable{

    private Double txn_amount;

    private Double refund_amount;

    private String product_code;

    private String esewa_id;

    private Boolean is_refund;

    private String remarks;

    private Properties properties;
}
