package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorSalutationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "doctor_salutation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorSalutationEntityListener.class)
public class DoctorSalutation extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "salutation_id")
    private Long salutationId;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "DoctorSalutation{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", salutationId=" + salutationId +
                ", status=" + status +
                ", remarks='" + remarks +
                '}';
    }

}
