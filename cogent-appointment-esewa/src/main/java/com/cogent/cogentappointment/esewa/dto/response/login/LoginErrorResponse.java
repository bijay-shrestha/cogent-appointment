package com.cogent.cogentappointment.esewa.dto.response.login;

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
