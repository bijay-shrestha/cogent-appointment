package com.cogent.cogentappointment.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Entity
@Table(name = "hospital")
@Getter
@Setter
public class Hospital implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Size(max = 4)
    private String code;

    @Column(name = "address")
    private String address;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
