package com.cogent.cogentappointment.thirdparty.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginErrorResponse {
    private int status;
    private String errorMessage;
    private String token;

}
