package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransactionDetailEntityListener;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransferTransactionDetailEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/6/20
 *  TO TRACK PREVIOUS APPOINTMENT TRANSACTION DETAILS
 * i.e Charges(including tax and discount),TransactionDate,TransactionTime
 */
@Entity
@Table(name = "appointment_transfer_transaction_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransferTransactionDetailEntityListener.class)
public class AppointmentTransferTransactionDetail extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_transaction_detail_id")
    private AppointmentTransactionDetail appointmentTransactionDetail;

    @Temporal(TemporalType.DATE)
    @Column(name = "previous_transaction_date")
    private Date previousTransactionDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "previous_transaction_date_time")
    private Date previousTransactionDateTime;

    @Column(name = "current_appointment_amount")
    private Double currentAppointmentAmount;

    @Column(name = "previous_appointment_amount")
    private Double previousAppointmentAmount;

    @Column(name = "previous_tax_amount")
    private Double previousTaxAmount;

    @Column(name = "previous_discount_amount")
    private Double previousDiscountAmount;

    @Column(name = "previous_service_charge_amount")
    private Double previousServiceChargeAmount;

    @Column(name = "remarks")
    private String remarks;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="appointment_transfer_id")
    private AppointmentTransfer appointmentTransfer;

    @Override
    public String toString() {
        return "AppointmentTransferTransactionDetail{" +
                "id=" + id +
                ", appointmentTransactionDetail=" + appointmentTransactionDetail.getTransactionNumber() +
                ", previousTransactionDate=" + previousTransactionDate +
                ", previousTransactionDateTime=" + previousTransactionDateTime +
                ", currentAppointmentAmount=" + currentAppointmentAmount +
                ", previousAppointmentAmount=" + previousAppointmentAmount +
                ", previousTaxAmount=" + previousTaxAmount +
                ", previousDiscountAmount=" + previousDiscountAmount +
                ", previousServiceChargeAmount=" + previousServiceChargeAmount +
                ", remarks='" + remarks + '\'' +
                ", appointmentTransfer=" + appointmentTransfer.getId() +
                '}';
    }
}
