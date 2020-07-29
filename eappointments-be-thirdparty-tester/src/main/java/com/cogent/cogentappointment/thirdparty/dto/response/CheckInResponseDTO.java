package com.cogent.cogentappointment.thirdparty.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckInResponseDTO implements Serializable {
    private Integer statusCode;
    private String responseData;
    private String responseMessage;
}
