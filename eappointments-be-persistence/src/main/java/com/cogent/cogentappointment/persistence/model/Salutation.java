package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.SalutationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "salutation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(SalutationEntityListener.class)
public class Salutation extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;

    @Column(name = "code")
    private String code;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "Salutation{" +
                "id=" + id +
                ", name='" + name +
                ", code='" + code +
                ", status=" + status +
                ", remarks='" + remarks +
                '}';
    }

}
