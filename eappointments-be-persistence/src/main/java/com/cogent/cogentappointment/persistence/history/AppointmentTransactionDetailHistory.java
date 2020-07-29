package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
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
 * @author Sauravi Thapa २०/२/३
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_transaction_detail_history")
public class AppointmentTransactionDetailHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_transaction_detail_id", foreignKey = @ForeignKey(name = "FK_transaction_detail_history_app_transaction_detail"))
    private AppointmentTransactionDetail appointmentTransactionDetail;

    @Column(name = "appointment_transaction_detail_content")
    @Lob
    private String appointmentTransactionDetailContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentTransactionDetailHistory(AppointmentTransactionDetail appointmentTransactionDetail, Action action) {
        this.appointmentTransactionDetail = appointmentTransactionDetail;
        this.appointmentTransactionDetailContent = appointmentTransactionDetail.toString();
        this.action = action;
    }

}
