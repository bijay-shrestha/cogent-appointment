package com.cogent.cogentappointment.admin.dto.request.login;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/१/१३
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO implements Serializable {

    private String email;

    private String password;
}
