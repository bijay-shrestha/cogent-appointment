package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "doctor_salutation_history")
public class DoctorSalutationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_salutation_id",
            foreignKey = @ForeignKey(name = "FK_doctor_salutation_history_doctor_salutation"))
    private DoctorSalutation doctorSalutation;

    @Column(name = "doctor_salutation_content")
    @Lob
    private String doctorSalutationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorSalutationHistory(DoctorSalutation doctorSalutation, Action action) {
        this.doctorSalutation = doctorSalutation;
        this.doctorSalutationContent = doctorSalutation.toString();
        this.action = action;
    }

}
