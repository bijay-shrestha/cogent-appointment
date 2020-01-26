package com.cogent.cogentappointment.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 17/11/2019
 */
@Getter
@Setter
@Table(name = "admin_profile")
@Entity
public class AdminProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "status")
    private Character status;
}
