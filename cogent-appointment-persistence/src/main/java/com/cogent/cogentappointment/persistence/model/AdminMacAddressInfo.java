package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminMacAddressInfoEntityListener;
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
@EntityListeners(AdminMacAddressInfoEntityListener.class)
public class AdminMacAddressInfo extends Auditable<String> implements Serializable {

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

    @Override
    public String toString() {
        return "AdminMacAddressInfo{" +
                "id=" + id +
                ", macAddress='" + macAddress + '\'' +
                ", status=" + status +
                ", admin=" + admin.getUsername() +
                '}';
    }
}
