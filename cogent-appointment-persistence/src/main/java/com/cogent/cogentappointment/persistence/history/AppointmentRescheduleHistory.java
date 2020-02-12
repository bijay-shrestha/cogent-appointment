package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentReschedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Rupak
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_reschedule_log_history")
public class AppointmentRescheduleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_reschedule_log_id",
            foreignKey = @ForeignKey(name = "FK_reschedule_log_history_reschedule_log"))
    private AppointmentReschedule appointmentReschedule;

    @Column(name = "appointment_refund_detail_content")
    @Lob
    private String appointmentRefundDetailContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentRescheduleHistory(AppointmentReschedule appointmentReschedule, Action action) {
        this.appointmentReschedule = appointmentReschedule;
        this.appointmentRefundDetailContent = appointmentReschedule.toString();
        this.action = action;
    }
}
