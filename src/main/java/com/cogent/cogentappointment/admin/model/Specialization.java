package com.cogent.cogentappointment.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 2019-09-25
 */
@Table(name = "specialization")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialization implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
