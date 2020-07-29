package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransactionRequestLogEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 29/03/20
 *
 * TO TRACK TRANSACTION STATUS OF SPECIFIC TRANSACTION NUMBER + PATIENT NAME (UNIQUE IDENTIFIER).
 * USEFUL TO VERIFY THE TRANSACTION STATUS ON OUR SIDE.
 * IN CASE OF TIMEOUT ISSUES, SERVER PROBLEM ETC. transactionStatus= 'N'
 * ELSE,
 *  transactionStatus= 'Y'
 */
@Entity
@Table(name = "appointment_transaction_request_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransactionRequestLogEntityListener.class)
public class AppointmentTransactionRequestLog extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date")
    private Date transactionDate;

    /*Y = SUCCESSFUL APPOINTMENT TRANSACTION
    * N = FAILED APPOINTMENT TRANSACTION
    * BY DEFAULT IT IS 'N'
    * */
    @Column(name = "transaction_status")
    private Character transactionStatus = 'N';

    @Column(name = "has_transferred")
    private Character has_transferred = 'N';

    @Override
    public String toString() {
        return "AppointmentTransactionRequestLog{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", transactionNumber='" + transactionNumber + '\'' +
                ", transactionDate=" + transactionDate +
                ", transactionStatus=" + transactionStatus +
                ", has_transferred=" + has_transferred +
                '}';
    }
}
