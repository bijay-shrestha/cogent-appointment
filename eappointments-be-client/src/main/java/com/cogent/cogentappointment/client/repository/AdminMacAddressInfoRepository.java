package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AdminMacAddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMacAddressInfoRepository extends JpaRepository<AdminMacAddressInfo, Long> {
}
