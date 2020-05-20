package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentChargeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
public interface HospitalDepartmentChargeRepository extends JpaRepository<HospitalDepartmentCharge,Long>,
        HospitalDepartmentChargeRepositoryCustom {
}
