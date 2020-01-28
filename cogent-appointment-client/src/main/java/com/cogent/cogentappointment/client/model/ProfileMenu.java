package com.cogent.cogentappointment.client.model;

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
public class ProfileMenu implements Serializable {

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
}
