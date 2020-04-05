package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentRefundDetailEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 06/02/2020
 *
 * SAVED IN THIS TABLE WHEN USER CANCELS THEIR APPOINTMENT
 *
 * RELATION BETWEEN APPOINTMENT AND REFUNDED APPOINTMENT
 * HERE REFUNDED AMOUNT IS CALCULATED ON THE BASIS OF REFUND PERCENTAGE DONE DURING HOSPITAL/CLIENT SETUP
 */
@Entity
@Table(name = "appointment_refund_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentRefundDetailEntityListener.class)
public class AppointmentRefundDetail extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointmentId;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Column(name = "remarks")
    private String remarks;

    /*PA = Pending Approval
     * A = Approved
     * R = Rejected*/
    @Column(name = "status")
    private String status;

    @Temporal(TemporalType.DATE)
    @Column(name = "cancelled_date")
    private Date cancelledDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "refunded_date")
    private Date refundedDate;

    @Override
    public String toString() {
        return "AppointmentRefundDetail{" +
                "id=" + id +
                ", appointmentId=" + appointmentId.getAppointmentNumber() +
                ", refundAmount=" + refundAmount +
                ", remarks='" + remarks + '\'' +
                ", status='" + status + '\'' +
                ", cancelledDate=" + cancelledDate +
                ", refundedDate=" + refundedDate +
                '}';
    }

}
