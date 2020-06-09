package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 6/2/20
 */
public interface HospitalDepartmentBillingModeInfoRepository extends JpaRepository<HospitalDepartmentBillingModeInfo,
        Long>, HospitalDepartmentBillingModeInfoRepositoryCustom {

    @Query("SELECT hdc FROM HospitalDepartmentBillingModeInfo hdc WHERE" +
            " hdc.id = :id AND  hdc.hospitalDepartment.id = :hospitalDepartmentId AND hdc.status = 'Y'")
    Optional<HospitalDepartmentBillingModeInfo> fetchByIdAndHospitalDepartmentId(
            @Param("id") Long id, @Param("hospitalDepartmentId") Long hospitalDepartmentId);

}
