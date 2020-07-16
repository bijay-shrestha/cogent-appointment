package com.cogent.cogentthirdpartyconnector.request;

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
public class EsewaRefundRequestDTO implements Serializable {

    private String esewa_id;

    private String product_code;

    private String txn_code;

    private Double txn_amount;

    private Double refund_amount;

    private Boolean is_refund;

    private String remarks;

    private Properties properties;

}
