package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ProfileMenuEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 7/6/19
 */
@Entity
@Table(name = "profile_menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ProfileMenuEntityListener.class)
public class ProfileMenu extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "user_menu_id")
    private Long userMenuId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ProfileMenu{" +
                "id=" + id +
                ", profile=" + profile.getName() +
                ", parentId=" + parentId +
                ", userMenuId=" + userMenuId +
                ", roleId=" + roleId +
                ", status=" + status +
                '}';
    }
}
