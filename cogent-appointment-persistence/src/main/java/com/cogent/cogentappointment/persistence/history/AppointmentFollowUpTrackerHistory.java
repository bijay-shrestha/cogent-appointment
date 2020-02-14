package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
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
 * @author smriti ON 12/02/2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_follow_up_tracker_history")
public class AppointmentFollowUpTrackerHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_follow_up_tracker_id",
            foreignKey = @ForeignKey(name = "FK_follow_up_tracker_history_follow_up_tracker"))
    private AppointmentFollowUpTracker appointmentFollowUpTracker;

    @Column(name = "appointment_follow_up_tracker_content")
    @Lob
    private String appointmentFollowUpTrackerContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentFollowUpTrackerHistory(AppointmentFollowUpTracker appointmentFollowUpTracker, Action action) {
        this.appointmentFollowUpTracker = appointmentFollowUpTracker;
        this.appointmentFollowUpTrackerContent = appointmentFollowUpTracker.toString();
        this.action = action;
    }

}
