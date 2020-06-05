package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentBillingModeInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
@Entity
@Table(name = "hospital_department_billing_mode_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentBillingModeInfoEntityListener.class)
public class HospitalDepartmentBillingModeInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_id")
    private HospitalDepartment hospitalDepartment;

    @Column(name = "appointment_charge")
    private Double appointmentCharge;

    @Column(name = "appointment_follow_up_charge")
    private Double appointmentFollowUpCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_mode_id")
    private BillingMode billingMode;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "HospitalDepartmentBillingModeInfo{" +
                "id=" + id +
                ", hospitalDepartment=" + hospitalDepartment.getName() +
                ", appointmentCharge=" + appointmentCharge +
                ", appointmentFollowUpCharge=" + appointmentFollowUpCharge +
                ", billingMode=" + billingMode.getName() +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                '}';
    }
}
