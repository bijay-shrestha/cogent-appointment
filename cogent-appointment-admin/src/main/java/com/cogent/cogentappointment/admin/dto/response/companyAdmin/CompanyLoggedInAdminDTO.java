package com.cogent.cogentappointment.admin.dto.response.companyAdmin;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLoggedInAdminDTO implements Serializable{
    private Long id;
    private String username;
    private String password;
    private Character isCompany;
}
