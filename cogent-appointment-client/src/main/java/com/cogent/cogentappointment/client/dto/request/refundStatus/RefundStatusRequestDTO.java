package com.cogent.cogentappointment.client.dto.request.refundStatus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/25/20
 */

@Setter
@Getter
public class RefundStatusRequestDTO implements Serializable{

    @NotNull
    @NotEmpty
    @NotBlank
    private String esewaId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String esewaMerchantCode;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionNumber;


    @NotBlank
    private String status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
