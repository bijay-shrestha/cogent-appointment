package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentDoctorInfoRepositoryCustom;
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

    @Query(value = "SELECT hdi FROM HospitalDepartmentDoctorInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId " +
            "AND hdi.status!='D'")
    List<HospitalDepartmentDoctorInfo> fetchDoctorListByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId);

    @Query(value = "SELECT hdi FROM HospitalDepartmentDoctorInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId" +
            " AND hdi.doctor.id=:doctorId AND hdi.status!='D'")
    HospitalDepartmentDoctorInfo fetchDoctorByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId,
                                                                   @Param("doctorId") Long doctorId);
}
