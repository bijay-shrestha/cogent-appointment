package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AdminModeRequestHeaderRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminModeRequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AdminModeRequestHeaderRepository extends JpaRepository<AdminModeRequestHeader, Long>,
        AdminModeRequestHeaderRepositoryCustom {
}
