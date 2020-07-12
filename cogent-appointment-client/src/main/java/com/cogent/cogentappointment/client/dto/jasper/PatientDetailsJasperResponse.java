package com.cogent.cogentappointment.client.dto.jasper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 7/9/20
 */
@Getter
@Setter
public class PatientDetailsJasperResponse implements Serializable{

    private String patientName;

    private String contact;

    private String registrationNumber;

    private String esewaId;

    private String status;

    private String client;

    private String hospitalNumber;

    private String patientType;
}
