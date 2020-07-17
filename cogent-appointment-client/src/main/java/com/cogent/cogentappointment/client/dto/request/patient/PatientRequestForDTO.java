package com.cogent.cogentappointment.client.dto.request.patient;

import com.cogent.cogentappointment.client.constraintvalidator.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/02/20
 */
@Getter
@Setter
public class PatientRequestForDTO implements Serializable {

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String mobileNumber;

    @NotNull
    @Gender
    private Character gender;

    @NotNull
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date dateOfBirth;

    private String email;

    /*
    *This is created for eSewa's convience, hence not used in our side.
    * 2020-07-17
    * */
    private String eSewaId;

    private String address;
}
