package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminEntityListener;
import com.cogent.cogentappointment.persistence.listener.HospitalEntityListener;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/३
 */
@Table(name = "hospital")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    /*Y= IF COGENT ADMIN
     * N= IF HOSPITALS ADMIN*/
    @Column(name = "is_cogent_admin")
    private Character isCogentAdmin;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", address='" + address + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", isCogentAdmin=" + isCogentAdmin +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
