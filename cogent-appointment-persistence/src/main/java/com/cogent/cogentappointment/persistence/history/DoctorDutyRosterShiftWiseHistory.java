package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author smriti on 08/05/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "doctor_duty_roster_shift_wise_history")
public class DoctorDutyRosterShiftWiseHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ddr_shift_id",
            foreignKey = @ForeignKey(name = "FK_ddr_shift_wise_history"))
    private DoctorDutyRosterShiftWise doctorDutyRosterShiftWise;

    @Column(name = "ddr_shift_content")
    @Lob
    private String ddrShiftContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorDutyRosterShiftWiseHistory(DoctorDutyRosterShiftWise doctorDutyRosterShiftWise, Action action) {
        this.doctorDutyRosterShiftWise = doctorDutyRosterShiftWise;
        this.ddrShiftContent = doctorDutyRosterShiftWise.toString();
        this.action = action;
    }
}
