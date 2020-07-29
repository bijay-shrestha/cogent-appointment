package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentTransferTransactionRequestLog;
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
@Table(name = "appointment_transfer_transaction_request_log_history")
public class AppointmentTransferTransactionRequestLogHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_transfer_transaction_request_log_id")
    private AppointmentTransferTransactionRequestLog appointmentTransferTransactionRequestLog;

    @Column(name = "appointment_transfer_transaction_request_log_content")
    @Lob
    private String appointmentTransferTransactionRequestLogContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentTransferTransactionRequestLogHistory(
            AppointmentTransferTransactionRequestLog appointmentTransferTransactionRequestLog,
                                                              Action action) {
        this.appointmentTransferTransactionRequestLog = appointmentTransferTransactionRequestLog;
        this.appointmentTransferTransactionRequestLogContent = appointmentTransferTransactionRequestLog.toString();
        this.action = action;
    }

}
