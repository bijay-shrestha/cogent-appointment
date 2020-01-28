package com.cogent.cogentappointment.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class Qualification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "university")
    private String university;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country")
    private Country country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_alias")
    private QualificationAlias qualificationAlias;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
