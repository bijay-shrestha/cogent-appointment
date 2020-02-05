package com.cogent.cogentappointment.client.model;

import com.cogent.cogentappointment.client.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 14/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nationality")
public class Nationality extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
