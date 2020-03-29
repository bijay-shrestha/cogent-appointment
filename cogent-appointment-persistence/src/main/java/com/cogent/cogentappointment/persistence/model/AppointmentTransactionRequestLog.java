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
 * TO TRACK TRANSACTION STATUS OF SPECIFIC TRANSACTION NUMBER AND PATIENT NAME (UNIQUE IDENTIFIER)
 * USEFUL IN CASE OF TIMEOUT ISSUES
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

    @Column(name = "transaction_status")
    private Character transactionStatus;
}
