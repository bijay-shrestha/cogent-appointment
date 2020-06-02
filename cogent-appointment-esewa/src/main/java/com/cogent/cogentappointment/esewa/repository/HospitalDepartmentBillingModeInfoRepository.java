package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sauravi Thapa ON 6/2/20
 */
public interface HospitalDepartmentBillingModeInfoRepository extends JpaRepository<HospitalDepartmentBillingModeInfo,
        Long>,HospitalDepartmentBillingModeInfoRepositoryCustom {
}
