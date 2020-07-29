package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.SpecializationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 2019-09-25
 */
@Entity
@Table(name = "specialization")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(SpecializationEntityListener.class)
public class Specialization extends Auditable<String> implements Serializable {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Override
    public String toString() {
        return "Specialization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", hospital=" + hospital.getName() +
                '}';
    }
}
