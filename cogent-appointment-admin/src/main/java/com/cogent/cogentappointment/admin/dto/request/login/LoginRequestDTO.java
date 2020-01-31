package com.cogent.cogentappointment.admin.dto.request.login;

import lombok.*;

/**
 * @author Sauravi Thapa २०/१/१३
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    private String username;

    private String email;

    private String password;
}
