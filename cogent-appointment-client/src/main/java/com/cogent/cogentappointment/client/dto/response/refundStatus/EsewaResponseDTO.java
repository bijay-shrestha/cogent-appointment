package com.cogent.cogentappointment.client.dto.response.refundStatus;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/26/20
 */
@Getter
@Setter
public class EsewaResponseDTO implements Serializable {

    private String status;

    private String body;
}
