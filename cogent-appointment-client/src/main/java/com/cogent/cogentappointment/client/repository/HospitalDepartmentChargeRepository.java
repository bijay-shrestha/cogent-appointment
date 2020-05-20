package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentChargeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
public interface HospitalDepartmentChargeRepository extends JpaRepository<HospitalDepartmentCharge,Long>,
        HospitalDepartmentChargeRepositoryCustom {

    @Query(value = "SELECT hdc FROM HospitalDepartmentCharge hdc WHERE hdc.hospitalDepartment.id = :id AND hdc.status != 'D'")
    Optional<HospitalDepartmentCharge> fetchByHospitalDepartmentId(@Param("hospitalDepartmentId") Long id);
}
