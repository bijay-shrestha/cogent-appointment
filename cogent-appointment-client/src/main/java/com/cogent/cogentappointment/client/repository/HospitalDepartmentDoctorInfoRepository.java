package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentDoctorInfoRepository extends JpaRepository<HospitalDepartmentDoctorInfo,Long>,
        HospitalDepartmentDoctorInfoRepositoryCustom {


    @Query(value = "SELECT hdi.doctor.id FROM HospitalDepartmentDoctorInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId AND hdi.status='Y'")
    List<Long> fetchDoctorIdListByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId);
}
