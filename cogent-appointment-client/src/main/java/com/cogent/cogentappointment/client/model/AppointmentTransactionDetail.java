package com.cogent.cogentappointment.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 04/02/2020
 */
@Entity
@Table(name = "appointment_transaction_detail")
@Getter
@Setter
public class AppointmentTransactionDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date transactionDate;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "appointment_amount")
    private Double appointmentAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "service_charge_amount")
    private Double serviceChargeAmount;
}
