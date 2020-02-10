package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
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
 * @author smriti ON 06/02/2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_refund_detail_history")
public class AppointmentRefundDetailHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_refund_detail_id",
            foreignKey = @ForeignKey(name = "FK_appointment_refund_detail_history_appointment_refund_detail"))
    private AppointmentRefundDetail appointmentRefundDetail;

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

    public AppointmentRefundDetailHistory(AppointmentRefundDetail appointmentRefundDetail, Action action) {
        this.appointmentRefundDetail = appointmentRefundDetail;
        this.appointmentRefundDetailContent = appointmentRefundDetail.toString();
        this.action = action;
    }
}
