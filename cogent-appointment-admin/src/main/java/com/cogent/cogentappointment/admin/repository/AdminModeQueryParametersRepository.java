package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AdminModeQueryParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminModeQueryParametersRepository extends JpaRepository<AdminModeQueryParameters,Long>{
}
