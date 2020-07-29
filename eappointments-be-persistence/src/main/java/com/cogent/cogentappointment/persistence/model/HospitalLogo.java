package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalLogoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Entity
@Table(name = "hospital_logo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HospitalLogoEntityListener.class)
public class HospitalLogo extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_uri")
    private String fileUri;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "status")
    private Character status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Override
    public String toString() {
        return "HospitalLogo{" +
                "id=" + id +
                ", fileUri='" + fileUri + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", status=" + status +
                ", hospital=" + hospital.getName() +
                '}';
    }
}
