package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransactionRequestLogEntityListener;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransferTransactionRequestLogEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/6/20
 *
 * TO TRACK PREVIOUS APPOINTMENT TRANSACTION REQUEST LOG DETAILS
 * i.e TransactionDate,Status
 */
@Entity
@Table(name = "appointment_transfer_transaction_request_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransferTransactionRequestLogEntityListener.class)
public class AppointmentTransferTransactionRequestLog extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "previous_transaction_date")
    private Date previousTransactionDate;

    /*Y = SUCCESSFUL APPOINTMENT TRANSACTION
    * N = FAILED APPOINTMENT TRANSACTION
    * BY DEFAULT IT IS 'N'
    * */
    @Column(name = "previous_transaction_status")
    private Character previousTransactionStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_transaction_request_log_id")
    private AppointmentTransactionRequestLog appointmentTransactionRequestLog;

    @Column(name = "remarks")
    private Character remarks;

    @Override
    public String toString() {
        return "AppointmentTransferTransactionRequestLog{" +
                "id=" + id +
                ", previousTransactionDate=" + previousTransactionDate +
                ", previousTransactionStatus=" + previousTransactionStatus +
                ", appointmentTransactionRequestLog=" + appointmentTransactionRequestLog.getTransactionNumber() +
                ", remarks=" + remarks +
                '}';
    }
}
