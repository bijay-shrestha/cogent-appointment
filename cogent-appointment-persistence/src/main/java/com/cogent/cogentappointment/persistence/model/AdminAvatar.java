
package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminAvatarEntityListener;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "admin_avatar")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AdminAvatarEntityListener.class)
public class AdminAvatar extends Auditable<String> implements Serializable {
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
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Override
    public String toString() {
        return "AdminAvatar{" +
                "id=" + id +
                ", fileUri='" + fileUri + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", status=" + status +
                ", admin=" + admin.getFullName() +
                '}';
    }
}
