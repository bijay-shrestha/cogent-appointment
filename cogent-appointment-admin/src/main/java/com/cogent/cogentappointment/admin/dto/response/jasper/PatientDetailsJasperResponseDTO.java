package com.cogent.cogentappointment.admin.dto.response.jasper;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 7/9/20
 */
@Getter
@Setter
public class PatientDetailsJasperResponseDTO implements Serializable{

    private String client;

    private String patientType;

    private String patientName;

    private String contact;

    private String registrationNumber;

    private String eSewaId;

    private String status;

    private String hospitalNumber;

    private Integer totalItems;
}
