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

    private Double txn_amount;

    private Double refund_amount;

    private String product_code;

    private String esewa_id;

    private Boolean is_refund;

    private String remarks;

    private Properties properties;

    @Override
    public String toString() {
        return "EsewaRefundRequestDTO{" +
                "txn_amount=" + txn_amount +
                ", refund_amount=" + refund_amount +
                ", product_code='" + product_code + '\'' +
                ", esewa_id='" + esewa_id + '\'' +
                ", is_refund=" + is_refund +
                ", remarks='" + remarks + '\'' +
                ", properties=" + "hospitalName= " +properties.getHospitalName() + ","
                + "appointmentId= " + properties.getAppointmentId() +
                '}';
    }
}
