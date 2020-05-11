package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorQualificationEntityListener;
import com.cogent.cogentappointment.persistence.listener.DoctorShiftEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*SHIFTS CAN BE ASSIGNED TO A DOCTOR FROM DDR SETUP ALSO (ONLY ADD)*/

/**
 * @author smriti on 06/05/20
 */
@Entity
@Table(name = "doctor_shift")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorShiftEntityListener.class)
public class DoctorShift extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "DoctorShift{" +
                "id=" + id +
                ", doctor=" + doctor.getName() +
                ", shift=" + shift.getName() +
                ", status=" + status +
                '}';
    }
}
