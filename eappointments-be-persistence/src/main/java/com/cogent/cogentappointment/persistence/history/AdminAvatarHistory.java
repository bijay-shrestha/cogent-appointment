package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminAvatar;
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
@Table(name = "admin_avatar_history")
public class AdminAvatarHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_avatar_id",foreignKey = @ForeignKey(name = "FK_admin_avatar_history_admin_avatar"))
    private AdminAvatar adminAvatar;

    @Column(name = "admin_avatar_content")
    @Lob
    private String adminAvatarContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminAvatarHistory(AdminAvatar adminAvatar,Action action) {
        this.adminAvatar=adminAvatar;
        this.adminAvatarContent=adminAvatar.toString();
        this.action=action;
    }
}
