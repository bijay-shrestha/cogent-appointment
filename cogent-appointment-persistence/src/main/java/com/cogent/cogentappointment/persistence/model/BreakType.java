package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.BreakTypeEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 06/05/20
 */
@Entity
@Table(name = "break_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(BreakTypeEntityListener.class)
public class BreakType extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_default")
    private Character isDefault;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "BreakType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDefault='" + isDefault + '\'' +
                ", hospital='" + hospital.getName() + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
