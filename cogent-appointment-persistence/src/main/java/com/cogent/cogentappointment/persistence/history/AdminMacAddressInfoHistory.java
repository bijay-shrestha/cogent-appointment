package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminMacAddressInfo;
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
 * @author Sauravi Thapa २०/२/४
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_mac_address_info_history")
public class AdminMacAddressInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_mac_address_info_id",
            foreignKey = @ForeignKey(name = "FK_admin_mac_history_admin_mac"))
    private AdminMacAddressInfo adminMacAddressInfo;

    @Column(name = "admin_avatar_content")
    @Lob
    private String adminMacAddressInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminMacAddressInfoHistory(AdminMacAddressInfo adminMacAddressInfo, Action action) {
        this.adminMacAddressInfo = adminMacAddressInfo;
        this.adminMacAddressInfoContent = adminMacAddressInfo.toString();
        this.action = action;
    }
}
