package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.BillingModeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
@Entity
@Table(name = "billing_mode")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(BillingModeListener.class)
public class BillingMode extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "code", nullable = false, updatable = false, length = 50)
    private String code;

    @Column(name = "status")
    private Character status;

    @Length(max = 100)
    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "billingMode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
