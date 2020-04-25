package com.cogent.cogentappointment.client.dto.request.login;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/3/20
 */
@Getter
@Setter
public class LoginEsewaRequestDTO implements Serializable
{
    private String companyCode;

}
