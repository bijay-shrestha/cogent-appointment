package com.cogent.cogentappointment.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Entity
@Table(name = "qualification")
@Getter
@Setter
public class Qualification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_alias")
    private QualificationAlias qualificationAlias;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university")
    private University university;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
