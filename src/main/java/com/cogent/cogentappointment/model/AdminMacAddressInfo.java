package com.cogent.cogentappointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "admin_mac_address_info")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminMacAddressInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "status")
    private Character status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

}
