package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminMetaInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 2019-08-27
 */
@Table(name = "admin_meta_info")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminMetaInfoEntityListener.class)
public class AdminMetaInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meta_info")
    private String metaInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Override
    public String toString() {
        return "AdminMetaInfo{" +
                "id=" + id +
                ", metaInfo='" + metaInfo + '\'' +
                ", admin=" + admin.getFullName() +
                '}';
    }
}
