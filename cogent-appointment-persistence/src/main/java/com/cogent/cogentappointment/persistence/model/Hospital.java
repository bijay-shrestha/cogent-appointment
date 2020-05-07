package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/३
 * THIS ENTITY IS USED TO SAVE BOTH HOSPITAL/CLIENT AND COMPANY
 * IN CASE OF COMPANY - isCompany = 'Y' ELSE isCompany = 'N'
 * REFUND PERCENTAGE AND FOLLOW UP DETAILS DO NOT NEED TO BE SAVED IN CASE OF COMPANY
 */
@Table(name = "hospital")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HospitalEntityListener.class)
public class Hospital extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "address")
    private String address;

    @Column(name = "pan_number")
    private String panNumber;

    /*Y= IF COMPANY(F1SOFT GROUP OF COMPANIES) ADMIN
     * N= IF HOSPITALS ADMIN*/
    @Column(name = "is_company")
    private Character isCompany;

    @Column(name = "status")
    private Character status;

    @Column(name = "refund_percentage")
    private Double refundPercentage;

    @Column(name = "number_of_admins")
    private Integer numberOfAdmins;

    @Column(name = "number_of_follow_ups")
    private Integer numberOfFollowUps;

    @Column(name = "follow_up_interval_days")
    private Integer followUpIntervalDays;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "alias", updatable = false)
    private String alias;

    @Column(name = "company_id")
    private Long companyId;

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name +
                ", code='" + code +
                ", address='" + address +
                ", panNumber='" + panNumber +
                ", isCompany=" + isCompany +
                ", status=" + status +
                ", refundPercentage=" + refundPercentage +
                ", numberOfAdmins=" + numberOfAdmins +
                ", numberOfFollowUps=" + numberOfFollowUps +
                ", followUpIntervalDays=" + followUpIntervalDays +
                ", remarks='" + remarks +
                '}';
    }
}
