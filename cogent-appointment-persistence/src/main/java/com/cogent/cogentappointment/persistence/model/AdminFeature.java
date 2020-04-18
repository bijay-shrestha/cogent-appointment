package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminFeatureEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 18/04/20
 * ALLOW ADMIN TO EITHER COLLAPSE SIDE BAR OR NOT BY THEIR OWN CHOICE
 */
@Entity
@Table(name = "admin_feature")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminFeatureEntityListener.class)
public class AdminFeature extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "is_side_bar_collapse")
    private Character isSideBarCollapse;
}
