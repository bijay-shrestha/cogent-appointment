package com.cogent.cogentappointment.client.model;

import com.cogent.cogentappointment.client.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 25/11/2019
 */
@Entity
@Table(name = "week_days")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekDays extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    private Character status;
}
