package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentTransferTransactionDetail;
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
 * @author Sauravi Thapa २०/२/३
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_transfer_transaction_detail_history")
public class AppointmentTransferTransactionDetailHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_transfer_transaction_detail_id")
    private AppointmentTransferTransactionDetail appointmentTransferTransactionDetail;

    @Column(name = "appointment_transfer_transaction_detail_content")
    @Lob
    private String appointmentTransferTransactionDetailContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentTransferTransactionDetailHistory(
            AppointmentTransferTransactionDetail appointmentTransferTransactionDetail,
            Action action) {
        this.appointmentTransferTransactionDetail = appointmentTransferTransactionDetail;
        this.appointmentTransferTransactionDetailContent = appointmentTransferTransactionDetail.toString();
        this.action = action;
    }

}
