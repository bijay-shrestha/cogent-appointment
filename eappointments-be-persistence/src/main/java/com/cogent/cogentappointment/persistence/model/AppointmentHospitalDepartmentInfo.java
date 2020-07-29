package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 28/05/20
 */
@Entity
@Table(name = "appointment_hospital_department_info")
@Getter
@Setter
public class AppointmentHospitalDepartmentInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_id")
    private HospitalDepartment hospitalDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_room_info_id")
    private HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_billing_mode_info_id")
    private HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo;
}
