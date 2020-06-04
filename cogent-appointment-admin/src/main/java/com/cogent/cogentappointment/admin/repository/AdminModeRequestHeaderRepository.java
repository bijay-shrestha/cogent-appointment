package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminModeRequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminModeRequestHeaderRepository extends JpaRepository<AdminModeRequestHeader, Long>{

}
