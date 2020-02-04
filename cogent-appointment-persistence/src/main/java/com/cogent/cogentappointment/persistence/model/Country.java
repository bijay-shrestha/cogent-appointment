package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author smriti on 2019-11-08
 */
@Entity
@Table(name = "country")
@Getter
@Setter
public class Country implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nice_name")
    private String niceName;

    @Column(name = "iso")
    private String iso;

    @Column(name = "iso_3")
    private String iso3;

    @Column(name = "num_code")
    private int numCode;

    @Column(name = "phone_code")
    private int phoneCode;

    @Column(name = "status")
    private Character status;
}
