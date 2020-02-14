package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpLog;
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
@Table(name = "appointment_follow_up_log_history")
public class AppointmentFollowUpLogHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_follow_up_log_id",
            foreignKey = @ForeignKey(name = "FK_appointment_follow_up_log_history"))
    private AppointmentFollowUpLog appointmentFollowUpLog;

    @Column(name = "appointment_follow_up_log_content")
    @Lob
    private String appointmentFollowUpLogContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentFollowUpLogHistory(AppointmentFollowUpLog appointmentFollowUpLog, Action action) {
        this.appointmentFollowUpLog = appointmentFollowUpLog;
        this.appointmentFollowUpLogContent = appointmentFollowUpLog.toString();
        this.action = action;
    }

}
