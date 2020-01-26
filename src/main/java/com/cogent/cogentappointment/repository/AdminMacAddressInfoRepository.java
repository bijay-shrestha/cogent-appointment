package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.AdminMacAddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMacAddressInfoRepository extends JpaRepository<AdminMacAddressInfo, Long> {
}
