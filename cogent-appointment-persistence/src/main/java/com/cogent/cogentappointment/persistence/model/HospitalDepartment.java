package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/19/20
 * <p>
 * CONNECTED TABLE :
 * 1. HospitalDepartmentBillingModeInfo
 * 2. HospitalDepartmentRoomInfo
 * 3. HospitalDepartmentDoctorInfo
 */
@Entity
@Table(name = "hospital_department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentEntityListener.class)
public class HospitalDepartment extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "department_name_in_nepali")
    private String departmentNameInNepali;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "code", nullable = false, updatable = false, length = 50)
    private String code;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Override
    public String toString() {
        return "HospitalDepartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", departmentNameInNepali='" + departmentNameInNepali + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", hospital=" + hospital.getName() +
                '}';
    }
}
