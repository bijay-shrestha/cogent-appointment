package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ProfileEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 7/2/19
 *
 * THIS ENTITY IS USED TO SAVE BOTH HOSPITAL/CLIENT PROFILE AND COMPANY PROFILE
 * IN CASE OF COMPANY PROFILE -> isCompanyProfile = 'Y' AND HOSPITAL IS NOT NULL AND DEPARTMENT IS NULL
 * ELSE isCompanyProfile ='N' AND DEPARTMENT IS NOT NULL AND HOSPITAL IS NULL
 */
@Entity
@Table(name = "profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ProfileEntityListener.class)
public class Profile extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Character status;

    /*Y-> COMPANY PROFILE
    * N-> HOSPITAL OR CLIENT PROFILE*/
    @Column(name = "is_company_profile")
    private Character isCompanyProfile;

    /*NOTE THAT THIS FLAG IS 'Y' FOR DEFAULT PROFILE SAVED IN DATABASE MANUALLY
      WHICH CANNOT BE UPDATED. ALSO, ADMIN WHO HAS THIS PROFILE CANNOT BE DELETED
      */
    @Column(name = "is_super_admin_profile", updatable = false)
    private Character isSuperAdminProfile;

    /*THIS IS HOSPITAL RELATION FOR HOSPITAL/CLIENT PROFILE*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    /*THIS IS COMPANY FOR COMPANY PROFILE*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Hospital company;

    @Column(name = "remarks")
    private String remarks;

    /*DEPARTMENT/HOSPITAL NOT ADDED SINCE ONE OR THE OTHER IS NULL*/
    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", isCompanyProfile='" + isCompanyProfile + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
