package com.cogent.cogentappointment.esewa.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/3/20
 */
@Getter
@Setter
public class LoginRequestDTO implements Serializable
{
    private String companyCode;

}
