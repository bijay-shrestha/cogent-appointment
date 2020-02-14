package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentRescheduleLog;
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
@Table(name = "appointment_reschedule_log_history")
public class AppointmentRescheduleLogHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_reschedule_log_id",
            foreignKey = @ForeignKey(name = "FK_reschedule_log_history_app_reschedule_log"))
    private AppointmentRescheduleLog appointmentRescheduleLog;

    @Column(name = "appointment_reschedule_log_content")
    @Lob
    private String appointmentRescheduleLogContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentRescheduleLogHistory(AppointmentRescheduleLog appointmentRescheduleLog, Action action) {
        this.appointmentRescheduleLog = appointmentRescheduleLog;
        this.appointmentRescheduleLogContent = appointmentRescheduleLog.toString();
        this.action = action;
    }

}
