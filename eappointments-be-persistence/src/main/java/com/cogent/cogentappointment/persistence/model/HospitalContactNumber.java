package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalContactNumberEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Entity
@Table(name = "hospital_contact_number")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HospitalContactNumberEntityListener.class)
public class HospitalContactNumber extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "HospitalContactNumber{" +
                "id=" + id +
                ", contactNumber='" + contactNumber + '\'' +
                ", hospitalId=" + hospitalId +
                ", status=" + status +
                '}';
    }
}
