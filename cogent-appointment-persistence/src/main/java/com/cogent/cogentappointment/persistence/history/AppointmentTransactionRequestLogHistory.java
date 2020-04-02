package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
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
 * @author smriti on 29/03/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_transaction_request_log_history")
public class AppointmentTransactionRequestLogHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_transaction_request_log_id",
            foreignKey = @ForeignKey(name = "FK_app_transaction_txn_request_log_history_app_transaction_txn_request_log"))
    private AppointmentTransactionRequestLog appointmentTransactionRequestLog;

    @Column(name = "appointment_transaction_request_log_content")
    @Lob
    private String appointmentTransactionRequestLogContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentTransactionRequestLogHistory(AppointmentTransactionRequestLog appointmentTransactionRequestLog,
                                                   Action action) {
        this.appointmentTransactionRequestLog = appointmentTransactionRequestLog;
        this.appointmentTransactionRequestLogContent = appointmentTransactionRequestLog.toString();
        this.action = action;
    }

}
