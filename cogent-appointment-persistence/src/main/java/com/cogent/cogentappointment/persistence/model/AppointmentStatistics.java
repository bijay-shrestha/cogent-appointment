package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminEntityListener;
import com.cogent.cogentappointment.persistence.listener.AppointmentStatisticsListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
@Entity
@Table(name = "appointment_statistics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AppointmentStatisticsListener.class)
public class AppointmentStatistics extends Auditable<String>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointmentId;

    @Column(name = "is_new")
    private Character isNew;

    @Column(name = "is_registered")
    private Character isRegistered;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_created_date")
    private Date appointmentCreatedDate;

    @Override
    public String toString() {
        return "AppointmentStatistics{" +
                "id=" + id +
                ", appointmentId=" + appointmentId.getId() +
                ", isNew=" + isNew +
                ", isRegistered=" + isRegistered +
                ", appointmentCreatedDate=" + appointmentCreatedDate +
                '}';
    }
}
