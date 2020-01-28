package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.model.AdminMacAddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMacAddressInfoRepository extends JpaRepository<AdminMacAddressInfo, Long> {
}
