package com.cogent.cogentappointment.admin.dto.request.refund.refundStatus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/25/20
 */

@Setter
@Getter
public class RefundStatusRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long hospitalId;

    @NotNull
    private Long appointmentModeId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String transactionNumber;

    private String appointmentMode;

    //FULL_REFUND, PARTIAL_REFUND, AMIBIGIUOS
    private String status;

    private String featureCode;

    private String integrationChannelCode;

    private String remarks;


}
