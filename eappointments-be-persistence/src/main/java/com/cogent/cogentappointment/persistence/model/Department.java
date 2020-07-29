package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DepartmentEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/*DEPARTMENT HAS BEEN RENAMED AS UNIT IN FRONT-END

IN ERROR MESSAGES, UNIT IS USED IN BACKEND ELSE NOTHING IS CHANGED
* */
@Entity
@Table(name = "department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DepartmentEntityListener.class)
public class Department extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "code", nullable = false, updatable = false, length = 50)
    private String code;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", hospital='" + hospital.getName() + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
