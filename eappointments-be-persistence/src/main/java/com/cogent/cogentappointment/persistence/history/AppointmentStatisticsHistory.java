package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentStatistics;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_statistics_history")
public class AppointmentStatisticsHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_statistics_id")
    private AppointmentStatistics appointmentStatistics;

    @Column(name = "appointment_statistics_content")
    @Lob
    private String appointmentStatisticsContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentStatisticsHistory(AppointmentStatistics appointmentStatistics,Action action) {
        this.appointmentStatistics=appointmentStatistics;
        this.appointmentStatisticsContent=appointmentStatistics.toString();
        this.action=action;
    }
}
