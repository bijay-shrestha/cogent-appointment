package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentWeekDaysDutyRosterDoctorInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 05/06/20
 */
@Table(name = "hospital_department_week_days_duty_roster_doctor_info")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentWeekDaysDutyRosterDoctorInfoEntityListener.class)
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "hospital_department_week_days_duty_roster_id")
    private HospitalDepartmentWeekDaysDutyRoster hospitalDepartmentWeekDaysDutyRoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "hospital_department_doctor_info_id")
    private HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo;

    @Column(name = "status")
    private Character status;
}
