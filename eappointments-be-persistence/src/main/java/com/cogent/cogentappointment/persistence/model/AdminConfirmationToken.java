package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminConfirmationTokenEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-22
 */
@Entity
@Table(name = "admin_confirmation_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminConfirmationTokenEntityListener.class)
public class AdminConfirmationToken extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AdminConfirmationToken{" +
                "id=" + id +
                ", admin=" + admin.getFullName() +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", status=" + status +
                '}';
    }
}
