package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentTransfer;
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
 * @author Sauravi Thapa ON 5/6/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_transfer_history")
public class AppointmentTransferHistory  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_transfer_id")
    private AppointmentTransfer appointmentTransfer;

    @Column(name = "appointment_transfer_content")
    @Lob
    private String appointmentTransferContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentTransferHistory(AppointmentTransfer appointmentTransfer, Action action) {
        this.appointmentTransfer = appointmentTransfer;
        this.appointmentTransferContent = appointmentTransfer.toString();
        this.action = action;
    }
}
