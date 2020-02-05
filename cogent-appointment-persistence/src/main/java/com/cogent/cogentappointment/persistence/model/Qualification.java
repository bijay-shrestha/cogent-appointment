package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.QualificationEntityListener;
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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(QualificationEntityListener.class)
public class Qualification extends Auditable<String> implements Serializable {

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

    @Override
    public String toString() {
        return "Qualification{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", qualificationAlias=" + qualificationAlias.getName() +
                ", university=" + university.getName() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
