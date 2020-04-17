package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentModeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
@Entity
@Table(name = "appointment_mode")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AppointmentModeListener.class)
public class AppointmentMode extends Auditable<String>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "is_editable")
    private Character isEditable;

    @Column(name = "status")
    private Character status;

    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "AppointmentMode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isEditable=" + isEditable +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
