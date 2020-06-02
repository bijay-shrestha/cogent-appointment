package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
public interface HospitalDepartmentBillingModeInfoRepository extends JpaRepository<HospitalDepartmentBillingModeInfo, Long>,
        HospitalDepartmentBillingModeInfoRepositoryCustom {


    @Query(value = "SELECT hdc FROM HospitalDepartmentBillingModeInfo hdc WHERE" +
            " hdc.hospitalDepartment.id = :hospitalDepartmentId AND hdc.status != 'D'")
    Optional<HospitalDepartmentBillingModeInfo> fetchByHospitalDepartmentId(@Param("hospitalDepartmentId")
                                                                                    Long hospitalDepartmentId);

    @Query(value = "SELECT hdc FROM HospitalDepartmentBillingModeInfo hdc WHERE hdc.id=:id AND hdc.status != 'D'")
    HospitalDepartmentBillingModeInfo fetchById(@Param("id") Long id);

}
