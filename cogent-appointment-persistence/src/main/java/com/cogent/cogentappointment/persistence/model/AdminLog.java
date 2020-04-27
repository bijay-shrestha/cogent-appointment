package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminLogEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Table(name = "admin_log")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AdminLogEntityListener.class)
public class AdminLog extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin adminId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "role_id")
    private Long roleId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "log_date_time")
    private Date logDateTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "log_date")
    private Date logDate;

    @Column(name = "log_description")
    private String logDescription;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "feature")
    private String feature;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "browser")
    private String browser;

    @Column(name = "location")
    private String location;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AdminLog{" +
                "id=" + id +
                ", adminId='" + adminId.getId() + '\'' +
                ", parentId='" + parentId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", logDateTime='" + logDateTime + '\'' +
                ", logDate='" + logDate + '\'' +
                ", logDescription='" + logDescription + '\'' +
                ", feature='" + feature + '\'' +
                ", actionType='" + actionType + '\'' +
                ", browser='" + browser + '\'' +
                ", location='" + location + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
