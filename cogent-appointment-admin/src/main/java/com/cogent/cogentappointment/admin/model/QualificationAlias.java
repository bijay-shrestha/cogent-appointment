package com.cogent.cogentappointment.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 08/11/2019
 */
@Entity
@Table(name = "qualification_alias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QualificationAlias implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;
}
