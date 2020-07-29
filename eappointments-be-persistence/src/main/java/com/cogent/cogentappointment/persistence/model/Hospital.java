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
 * <p>
 * CONNECTED TABLE:
 * a. HospitalContactNumber
 * b. HospitalLogo
 * c. HospitalBanner
 * d. HospitalAppointmentServiceType
 * <p>
 * HOSPITAL HAS BEEN RENAMED AS CLIENT SETUP IN FRONT-END
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

    @Column(name = "code", updatable = false)
    private String code;

    @Column(name = "esewa_merchant_code", updatable = false)
    private String esewaMerchantCode;

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
    private Double refundPercentage = 0D;

    @Column(name = "number_of_admins")
    private Integer numberOfAdmins = 0;

    @Column(name = "number_of_follow_ups")
    private Integer numberOfFollowUps = 0;

    @Column(name = "follow_up_interval_days")
    private Integer followUpIntervalDays = 0;

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
