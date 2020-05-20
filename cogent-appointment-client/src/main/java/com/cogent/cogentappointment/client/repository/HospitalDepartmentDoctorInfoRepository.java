package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentDoctorInfoRepository extends JpaRepository<HospitalDepartmentDoctorInfo,Long>,
        HospitalDepartmentDoctorInfoRepositoryCustom {
}
