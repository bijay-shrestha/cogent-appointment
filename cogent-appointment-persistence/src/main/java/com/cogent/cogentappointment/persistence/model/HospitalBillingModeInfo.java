package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalBillingModeInfoEntityListener;
import com.cogent.cogentappointment.persistence.listener.HospitalEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 29/05/2020
 * This is a normalize table of hospital and billing mode
 */
@Table(name = "hospital_billing_mode_info")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HospitalBillingModeInfoEntityListener.class)
public class HospitalBillingModeInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private Character remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_mode_id")
    private BillingMode billingMode;

    @Override
    public String toString() {
        return "HospitalBillingModeInfo{" +
                "id=" + id +
                ", status=" + status +
                ", remarks=" + remarks +
                ", hospital=" + hospital.getName() +
                ", billingMode=" + billingMode.getName() +
                '}';
    }
}
