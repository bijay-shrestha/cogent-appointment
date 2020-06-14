package com.cogent.cogentthirdpartyconnector.response.integrationBackend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/14-6:34 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BheriHospitalResponse implements Serializable{

    private String hospitalNumber;

    private String message;

}
