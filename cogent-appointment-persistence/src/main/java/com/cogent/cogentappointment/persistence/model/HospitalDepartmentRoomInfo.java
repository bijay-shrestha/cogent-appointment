package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentDoctorInfoEntityListener;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentRoomInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/19/20
 * Consists Room list as assigned in Department
 */
@Entity
@Table(name = "hospital_department_room_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentRoomInfoEntityListener.class)
public class HospitalDepartmentRoomInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_id")
    private HospitalDepartment hospitalDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "HospitalDepartmentRoomInfo{" +
                "id=" + id +
                ", hospitalDepartment=" + hospitalDepartment.getName() +
                ", room=" + room.getRoomNumber() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
