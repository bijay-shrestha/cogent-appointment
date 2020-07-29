package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminFavouriteEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/16-11:55 AM
 */
@Entity
@Table(name = "admin_favourite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminFavouriteEntityListener.class)
public class AdminFavourite extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin adminId;

    @Column(name = "user_menu_id")
    private Long userMenuId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AdminFavourite{" +
                "id=" + id +
                ", adminId=" + adminId.getId() +
                ", userMenuId=" + userMenuId +
                ", status='" + status + '\'' +
                '}';
    }


}
